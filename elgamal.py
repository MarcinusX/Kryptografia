import hashlib
import random
from fractions import gcd
from random import randrange

p = 0
g = 0
a = 0
k = 0
r = 0
b = 0
M = ''


def getP():
    return random.getrandbits(1024)


def findRandomInteger(maxValue):
    return long(randrange(maxValue / 100, maxValue))


# test of primality of given number n
def millerRabinPrimalityTest(n, k=1):
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


def findRandomP():
    found = False
    p = 0
    while not found:
        p = getP()
        found = millerRabinPrimalityTest(p)

    return p


def findPrivateKeys():
    global p
    p = findRandomP()
    global g
    g = findRandomInteger(p - 2)
    global a
    a = findRandomInteger(p - 2)
    return str(a), str(p), str(g)


def findPublicKeys():
    global p
    global g
    global a
    b = pow(g, a, p)
    return str(b), str(p), str(g)


def egcd(a, b):
    if a == 0:
        return (b, 0, 1)
    else:
        g, y, x = egcd(b % a, a)
        return (g, x - (b // a) * y, y)


def modinv(a, m):
    g, x, y = egcd(a, m)
    if g != 1:
        raise Exception('modular inverse does not exist')
    else:
        return x % m


def signMessage(message):
    global p
    global g
    global k
    global M
    M = message
    isFoundK = False
    while not isFoundK:
        k = findRandomInteger(p - 2)
        _gcd = gcd(k, p - 1)
        if _gcd == 1:
            isFoundK = True
    r = pow(g, k, p)
    temp = modinv(long(k), long(p) - 1)
    hashed_message = hashlib.sha1(str(message)).hexdigest()
    hashed_message = long(hashed_message, 16)
    temp1 = p - 1
    s = (temp * (hashed_message - a * r)) % temp1
    return str(hashed_message), str(r), str(s)


def verifySignature(*args):
    b = long(args[0])
    g = long(args[1])
    p = long(args[2])
    r = long(args[3])
    s = long(args[4])
    hashedMsg = hashlib.sha1(str(M)).hexdigest()
    hashedMsg = long(hashedMsg, 16)

    if r < p:

        temp1 = pow(b, r, p)
        temp2 = pow(r, s, p)
        x1 = (temp1 * temp2) % p
        x2 = pow(g, hashedMsg, p)
        if x1 == x2:
            return True
        else:
            return False
    else:
        return False
