## Linear Congruential Generators
"Linear" because `a * seed + b` is linear
"Congruential" because of the modulus

Seed update rule
`seed =: (a * seed + b) mod m`

Where `a`, `b` and `m` are constants in `java.util.Random`

Java random uses this in `next()` (the main rng, `nextLong` just wraps it)
`seed =: 25214903917 * seed + 11 mod 2^48`
From here on I'll use `a` to refer to `25214903917`, and `b` as `11` and `m` as `2^48`.

A big problem with java random is the numbers alternate between even and odd!
This is because if `seed` is odd, `seed * a` is odd, plus an odd `b` is even. modulo an even `m` is still even!
If `seed` is even, `seed * a` is even, plus an odd `b` is odd, modulo an even is still odd!

In general the same happens with remainder mod 4, mod 8, etc, the bottom bits repeat with small period.
To get around this Java only uses the upper 32 bits to make numbers


`nextLong` is the most used method in minecraft code
We need to predict how many times `next` is called before what we care about (eg. 11 eye portal frame).
luckily the methods on `Random` call `next` a constant number of times. and minecraft is programmed such that it also calls `next` a predictable number of times. (eg. 761 calls we don't care about in the case of portals.)

So we want a way to jump forward and backward in `next`.

1. Reversing it once
`newseed = (a*seed + b) mod m`
`(newseed - b) = a*seed mod m`
`a^{-1} (newseed - b) = seed mod m`
We found the seed in terms of the new seed! (`a^{-1}` exists since `a` and `m` are coprime)
(If we rename the variables and rearrange, we see that *every LCG has a reverse LCG*)

2. [Skipping calls](https://youtu.be/XVrR1WImOh8?list=PLke4P_1UHlmB8sB1oGdcea4SeBH0yZy5B&t=1525)
Distribute a bunch of times
`seed =: a^n * seed + b*(sum_{i=0}^{n-1} a^i) (mod m)`
`seed =: a^n * seed + b*((a^n - 1)/(a - 1)) (mod m)`


Final detail, java scrambles your initial seed to prevent silly seeds like `0` by taking `seed =: seed (xor) a`

But xor is trival to reverse if you know `a`, which we do since its hardcoded in java random.


## A General Seedfinding Problem

To have a desirable feature we want
`a < nextSomething() < b`
For all `next*` methods in java random we can find `a'` and `b'` such that
`a' < seed < b'` is the same thing.

More generally, we might have a system of inequalities with the seed after various numbers of steps,
and we want all of them to happen.

...I paused here to implement some stuff before getting too bogged down in theory
