import math
import random
from random import randrange


# checks if number a satisfies rabin condition: a == 3 (mod 4)
def checkIfSatisfyRabinPrivateKeyCondition(a):
    if a % 4 == 3:
        return True
    else:
        return False


# returns random prime number from range (10000, 100000)
def findRandomPrime():
    while True:
        n = random.randint(10000000, 10000000000)
        if n % 2 == 0:
            continue

        prime = True

        for x in range(3, int(n ** 0.5 + 1), 2):
            if n % x == 0:
                prime = False
                break

        if prime:
            return n


# test of primality of given number n
def millerRabinPrimalityTest(n, k=10):
    if n == 2:
        return True
    if not n & 1:
        return False

    def check(a, s, d, n):
        x = pow(a, d, n)
        if x == 1:
            return True
        for i in range(s - 1):
            if x == n - 1:
                return True
            x = pow(x, 2, n)
        return x == n - 1

    s = 0
    d = n - 1

    while d % 2 == 0:
        d >>= 1
        s += 1

    for i in range(k):
        a = randrange(2, n - 1)
        if not check(a, s, d, n):
            return False
    return True


# finds private key: p and q numbers
def findPrivateKey():
    p = 0
    q = 0
    foundP = False
    foundQ = False

    while (foundP == False):
        p = findRandomPrime()
        foundP = checkIfSatisfyRabinPrivateKeyCondition(p)

    while (foundQ == False):
        q = findRandomPrime()
        foundQ = checkIfSatisfyRabinPrivateKeyCondition(q)

    return {'p': p, 'q': q}


# m - message
# n = p * g - public key
# returns encrypted message
def encrypt(m, n):
    return ((math.pow(m, 2)) % n)


# returns g, x, y which safisfy equation px + qy = 1
def extendedEuclidAlgorithm(p, q):
    if p == 0:
        return (q, 0, 1)
    else:
        g, y, x = extendedEuclidAlgorithm(q % p, p)
        return (g, x - (q // p) * y, y)


# returns computed four square roots, from which one is encrypted message
def decrypt(p, q, c):
    (g, x, y) = extendedEuclidAlgorithm(p, q)
    c = int(c)

    # calculates public key : n = p * q
    n = p * q

    # computes square roots: m_p = c^(1/4 *(p + 1)), m_q = c^(1/4 *(q + 1))
    m_p = (pow(c, ((p + 1) // 4), p))
    m_q = (pow(c, ((q + 1) // 4), q))

    # calculates four square roots by using Chinese remainder theorem
    r1 = ((x * p * m_q) + (y * q * m_p)) % n
    r2 = ((x * p * m_q) - (y * q * m_p)) % n
    r3 = (-r1) % n
    r4 = (-r2) % n

    return r1, r2, r3, r4
