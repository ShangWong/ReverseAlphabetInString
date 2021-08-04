# Reverse Alphabet in String Speed Comparison

## TL;DR
This repo demonstrates that for a simple String reverse task (e.g. `"Hello, my name is Jack!"` -> `"olleH, ym eman si kcaJ!"`), by taking advantage of multi-thread approach, the process time could be **faster** than the single-thread approach under the certain condition.

## Task Defination
The task is to reverse every word in a given String, and leave the non-alphabet and non-number characters where they were.

## Test Result
The test result I got from a MacBook Pro 2017, which has a dual-core Intel Core i5 2.3GHz CPU, shows that multi-thread approach will be **120% faster** (on average) than single-thread approach when sample string's length is _**around 4750**_. 


| No.  	|  1st 	|   2nd	|   3rd	|  4th 	|  5th 	|  6th 	|  7th 	|  8th 	|  9th 	|  10th 	| 
|---	|---	|---	|---	|---	|---	|---	|---	|---	|---	|---	|
|  Single-thread (ns)	| 1142165  	|   		1036354	| 1590498| 	721331| 	1187099	| 879516| 	1172706	| 1292687| 	1257573	| 961192	 	| 
|   Multi-thread (ns)	| 981235  	|   		937298	| 899893| 	634869	| 1083479	| 799747| 	918943| 	1277974| 	1035790	| 823764	 	| 
|  Faster by 	|  1.16 	|	1.11| 	1.77| 	1.14| 	1.10	| 1.10| 	1.28	| 1.01	| 1.21	|1.17	 	|
|  Text Length 	|  4940 | 	5320| 	2280| 	2280	| 4940| 	4180| 	9500	| 4180	| 6080	| 3800|

## Why only 120% faster (on average)
Compare to single-thread approach, the multi-thread approach has to 
1. calucate the divide points of the String
2. reverse the String in each thread task (same as single-thread)
3. join the Thread task's result in a certain order

Multi-thread approach accelerates the **step 2**, but has to cost a certain amount of time on **step 1 & 3**.

I assume, with the number of the working threads going up, the cost of **step 1 & 3** overall should go down.

## Future research
1. In the test above, I set my `threadPoolSize` as 2, I wonder how many times faster it will be if the PC has 4 or 8 cores.
2. More cores should reduce the Text Length before multi-thread could overrun single thread, how much more?
3. Determine the divide points efficiently could be hard, when working with more than 2 threads.
