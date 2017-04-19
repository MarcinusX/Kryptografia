import random
from random import randrange


# returns bytes from message
def stringToBytes(message):
    return bytearray(message, 'utf-8')

# checks if number a satisfies rabin condition: a == 3 (mod 4)
def checkIfSatisfyRabinPrivateKeyCondition(a):
    return a % 4 == 3


# returns random prime number from specified range
def findRandomPrime(maxValue):
    while True:
        n = long(random.randint(maxValue / 10, maxValue))
        if n % 2 == 0:
            continue

        prime = millerRabinPrimalityTest(n)

        # for x in range(3, int(n ** 0.5 + 1), 2):
        #     if n % x == 0:
        #         prime = False
        #         break

        if prime:
            return n

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

# finds private key: p and q numbers
def findPrivateKey(message):
    message = int(message)
    maxValue = message + 1
    p = 0L
    q = 0L
    foundP = False
    foundQ = False

    while (foundP == False):
        p = findRandomPrime(maxValue)
        foundP = checkIfSatisfyRabinPrivateKeyCondition(p)

    while (foundQ == False):
        q = findRandomPrime(maxValue)
        foundQ = checkIfSatisfyRabinPrivateKeyCondition(q)

    return str(p), str(q)


# returns public key n = p * q
def findPublicKey(p, q):
    return str(long(p) * long(q))

# m - message
# n = p * g - public key
# returns encrypted message
def encrypt(m, n):
    m = int(m)
    n = int(n)

    # calculates encrypted message equal to m ^ 2 mod n
    encrypted = str(pow(m, 2L, n))
    b0 = m % 2
    b1 = 0.5 * (1 + jacobi(m, n))
    return (encrypted, str(b0), str(int(b1)))

# returns g, x, y which safisfy equation px + qy = 1
def extendedEuclidAlgorithm(p, q):
    if p == 0:
        return (q, 0L, 1L)
    else:
        g, y, x = extendedEuclidAlgorithm(q % p, p)
        return (g, x - (q // p) * y, y)


# def xgcd(a, b):
#     if a == 0: return 0, 1, b
#     if b == 0: return 1, 0, a
#
#     px, ppx = 0, 1
#     py, ppy = 1, 0
#
#     while b:
#         q = a // b
#         a, b = b, a % b
#         x = ppx - q * px
#         y = ppy - q * py
#         ppx, px = px, x
#         ppy, py = py, y
#
#     return ppx, ppy, a

# returns computed four square roots, from which one is encrypted message
def decrypt(p, q, c, b01):
    p = int(p)
    q = int(q)
    c = int(c)
    b0 = b01[0: 1]
    b1 = b01[1:]

    # calculates extended euclid algorithm
    (g, x, y) = extendedEuclidAlgorithm(p, q)

    # calculates public key : n = p * q
    n = p * q

    # computes square roots: m_p = c^(1/4 *(p + 1)), m_q = c^(1/4 *(q + 1))
    m_p = int(pow(c, ((p + 1L) // 4L), p))
    m_q = int(pow(c, ((q + 1L) // 4L), q))

    # calculates four square roots by using Chinese remainder theorem
    r1 = (int(x * p * m_q) + int(y * q * m_p)) % n
    r2 = n - r1
    r3 = (int(x * p * m_q) - int(y * q * m_p)) % n
    r4 = n - r3

    # finds roots which have parity equal to b0
    parityEqualValues = []
    decryptedValue = 0
    if (isEqualToParityBit(r1, b0)):
        parityEqualValues.append(r1)

    if (isEqualToParityBit(r2, b0)):
        parityEqualValues.append(r2)

    if (isEqualToParityBit(r3, b0)):
        parityEqualValues.append(r3)

    if (isEqualToParityBit(r4, b0)):
        parityEqualValues.append(r4)

    # finds root which has jacobi symbol equal to b0 and parity equal to b1
    if isEqualToJacobiSymbol(jacobi(parityEqualValues[0], n), b1):
        decryptedValue = parityEqualValues[0]
    if isEqualToJacobiSymbol(jacobi(parityEqualValues[1], n), b1):
        decryptedValue = parityEqualValues[1]

    return str(decryptedValue)


# checks if n is equal to parity bit b0
def isEqualToParityBit(n, b0):
    return n % 2 == int(b0)


# checks if n is equal to jacobi bit b1
def isEqualToJacobiSymbol(n, b1):
    return (0.5 * (1 + n)) == int(b1)


# returns integer from text message
def getIntegerFromText(message):
    textToBytes = stringToBytes(message)
    bytesToLong = long(str(textToBytes).encode('utf-8'), 16)

    return str(bytesToInt)


#returns text from integer message
def getTextFromInteger(message):
    strToInt = int(message)
    intToBytes = bytes(strToInt)
    result = intToBytes.decode('utf-8')

    return result


# returns jacobi symbol for (a/n) , result set: {-1, 1}
def jacobi(a, n):
    s = 1
    while True:
        if n == 1: return s
        a = a % n
        if a == 0: return 0
        if a == 1: return s

        if a & 1 == 0:
            if n % 8 in (3, 5):
                s = -s
            a >>= 1
            continue

        if a % 4 == 3 and n % 4 == 3:
            s = -s

        a, n = n, a
    return
    # def jacobi(a, n):
    #     ans = 0
    #
    #     if a == 0:
    #         ans = 1 if n == 1 else 0
    #     elif a == 2:
    #         if n % 8 == 1 or n % 8 == 7:
    #             ans = 1
    #         elif n % 8 == 3 or n % 8 == 5:
    #             ans = -1
    #     elif  a >= n:
    #         ans = jacobi(a%n, n)
    #     elif a % 2 == 0:
    #         ans = jacobi(2,n)*jacobi(a/2, n)
    #     else:
    #         ans = -jacobi(n,a) if ( a % 4 == 3 and n % 4 == 3 ) else jacobi(n,a)
    #     return ans
