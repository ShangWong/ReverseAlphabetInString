package com.company;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;
import java.util.concurrent.*;

public class Main {

    public static void main(String[] args) {
        String sampleInput = "Hello, my name is Jack! I have 2 cats!";
        boolean singleThreadIsFaster = true;
        int start = 10;
        while (singleThreadIsFaster) {
            // Sample Text Preparation
            StringBuilder compareText = new StringBuilder();
            int i = 0;
            while (i < start) {
                compareText.append(sampleInput);
                i++;
            }

            // Single Thread Approach Process
            long startTime = System.nanoTime();
            String single = new SingleThreadSolution().reverseStr(compareText.toString());
            long endTime = System.nanoTime();
            long duration = endTime - startTime;

            // Multi Thread Approach Process
            long mtStartTime = System.nanoTime();
            String multi = new MultiThreadSolution().reverseStr(compareText.toString());
            long mtEndTime = System.nanoTime();
            long mtDuration = mtEndTime - mtStartTime;

            // Compare
            if (mtDuration < duration) {
                System.out.println("Single Thread Duration " + duration + " ns");
                System.out.println("Multi Thread Duration " + mtDuration + " ns");
                System.out.println("Sample Text Length : " + single.length());
                System.out.println("Single Thread & Multi Thread result is same? " + single.equals(multi));
                break;
            }

            // Tuning the Sample Text Length
            start += 10;
        }

    }

    public static class MultiThreadSolution {
        public String reverseStr(String str) {
            if (str == null) return "";
            String res = "";

            Integer threadPoolSize = 2;
            ExecutorService executor = Executors.newFixedThreadPool(threadPoolSize);
            List<MultiThreadTask> tasks = new ArrayList<MultiThreadTask>();
            int[][] idxArray =  getIdxArray(str);

            tasks.add(new MultiThreadTask(str, idxArray[0][0], idxArray[0][1]));
            tasks.add(new MultiThreadTask(str, idxArray[1][0], idxArray[1][1]));

            try {
                List<Future<StrPart>> futureList = executor.invokeAll(tasks, 3000, TimeUnit.MILLISECONDS);
                PriorityQueue<StrPart> queue = new PriorityQueue<>((o1, o2) -> {return o1.idx - o2.idx;});
                for (Future<StrPart> future : futureList) {
                    try {
                        queue.add(future.get());
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }
                // Combine every part of result in order
                while (!queue.isEmpty()) {
                    res += queue.poll().strPart;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                executor.shutdown();
            }
            return res;
        }

        private int[][] getIdxArray(String str) {
            int mid = str.length() / 2;
            while (mid >= 0 && Utility.isAlphaOrNum(str.charAt(mid))) {
                mid--;
            }
            return new int[][]{{0, mid}, {mid+1, str.length()-1}};
        }
    }

    public static class StrPart {
        int idx;
        String strPart;
    }

    public static class MultiThreadTask implements Callable<StrPart> {
        private String str;
        private int head;
        private int tail;

        public MultiThreadTask(String str, int head, int tail) {
            this.str = str;
            this.head = head;
            this.tail = tail;
        }

        @Override
        public StrPart call() throws Exception {
            return reverseSubStr(str, head, tail);
        }

        private StrPart reverseSubStr(String str, int head, int tail) {
            StringBuilder res = new StringBuilder();
            StringBuilder sb = new StringBuilder();
            for (int i = head; i <= tail; i++) {
                char curr = str.charAt(i);
                if (Utility.isAlphaOrNum(curr)) {
                    sb.insert(0, curr);
                } else {
                    res.append(sb.toString());
                    sb = new StringBuilder();
                    res.append(curr);
                }
            }
            res.append(sb.toString());
            StrPart part = new StrPart();
            part.strPart = res.toString();
            part.idx = head;
            return part;
        }
    }

    public static class SingleThreadSolution {
        public String reverseStr(String str) {
            if (str == null) return "";
            StringBuilder res = new StringBuilder();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < str.length(); i++) {
                char curr = str.charAt(i);
                if (Utility.isAlphaOrNum(curr)) {
                    sb.insert(0, curr);
                } else {
                    res.append(sb.toString());
                    sb = new StringBuilder();
                    res.append(curr);
                }
            }
            res.append(sb.toString());
            return res.toString();
        }
    }

    public static class Utility {
        public static boolean isAlphaOrNum(char curr) {
            if (curr >= 'a' && curr <= 'z' || curr >= 'A' && curr <= 'Z' || curr >= '0' && curr <= '9') return true;
            return false;
        }
    }
}
