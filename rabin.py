import random
from random import randrange


def stringToBytes(message):
    return bytearray(message, 'utf-8')


# checks if number a satisfies rabin condition: a == 3 (mod 4)
def checkIfSatisfyRabinPrivateKeyCondition(a):
    return a % 4 == 3


# returns random prime number from range (10000, 100000)
def findRandomPrime():
    while True:
        n = long(random.randint(10000000, 10000000000))
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
    p = 0L
    q = 0L
    foundP = False
    foundQ = False

    while (foundP == False):
        p = findRandomPrime()
        foundP = checkIfSatisfyRabinPrivateKeyCondition(p)

    while (foundQ == False):
        q = findRandomPrime()
        foundQ = checkIfSatisfyRabinPrivateKeyCondition(q)

    return str(p), str(q)


def findPublicKey(p, q):
    return str(long(p) * long(q))


# m - message
# n = p * g - public key
# returns encrypted message
def encrypt(m, n):
    return str(((pow(long(m), 2L)) % long(n)))


# returns g, x, y which safisfy equation px + qy = 1
def extendedEuclidAlgorithm(p, q):
    if p == 0:
        return (q, 0L, 1L)
    else:
        g, y, x = extendedEuclidAlgorithm(q % p, p)
        return (g, x - (q // p) * y, y)


# returns computed four square roots, from which one is encrypted message
def decrypt(p, q, c):
    p = long(p)
    q = long(q)
    c = long(c)

    print(p)
    print(q)
    print(c)

    (g, x, y) = extendedEuclidAlgorithm(p, q)
    print("g" + str(g))
    print("x" + str(x))
    print("c" + str(y))

    # calculates public key : n = p * q
    n = p * q
    print("c " + str(c))
    print("p " + str(p))
    print("q " + str(q))

    # computes square roots: m_p = c^(1/4 *(p + 1)), m_q = c^(1/4 *(q + 1))
    print(pow(c, ((p + 1) // 4), p));
    print(pow(c, ((p + 1L) // 4L), p));

    m_p = long(pow(c, ((p + 1L) // 4L), p))
    m_q = long(pow(c, ((q + 1L) // 4L), q))

    print("m_p " + str(m_p))
    print("m_q " + str(m_q))

    # calculates four square roots by using Chinese remainder theorem
    r1 = (long(x * p * m_q) + long(y * q * m_p)) % n
    r2 = (long(x * p * m_q) - long(y * q * m_p)) % n
    r3 = long(-r1) % n
    r4 = long(-r2) % n

    print(r1)
    print(r2)
    print(r3)
    print(r4)

    return r1, r2, r3, r4


def getIntegerFromText(message):
    textToBytes = stringToBytes(message)
    print(textToBytes)
    bytesToLong = long(str(textToBytes).encode('utf-8'), 16)

    return str(bytesToInt)


def getTextFromInteger(message):
    strToInt = int(message)
    intToBytes = bytes(strToInt)
    print(intToBytes)
    result = intToBytes.decode('utf-8')

    return result

def main(message):
    privateKey_p = privateKey['p']
    privateKey_q = privateKey['q']

    # print (bytesToInt)
    bytesFromInt = str(bytesToInt).encode();

    return (decrypt(privateKey_p, privateKey_q, encrypt(bytesToInt, privateKey_p * privateKey_q)))
