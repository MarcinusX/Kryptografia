import sys

from rabin import findPrivateKey, millerRabinPrimalityTest, decrypt, encrypt


def main(message):
    message = int(float(sys.argv[1]))
    privateKey = findPrivateKey()
    privateKey_p = privateKey['p']
    print (millerRabinPrimalityTest(privateKey_p, 10))
    privateKey_q = privateKey['q']

    decrypt(privateKey_p, privateKey_q, encrypt(message, privateKey_p * privateKey_q))

    print(int(5))


main(message=5)
