# BLS12-381 Implementation for Java

NOTE: THIS LIBRARY IS NOT YET FORMALLY REVIEWED FOR SECURITY

Implements BLS signatures with aggregation compatible with [relic toolkit](https://github.com/relic-toolkit/relic)
for cryptographic primitives (pairings, EC, hashing) according to the
[IETF BLS RFC](https://datatracker.ietf.org/doc/draft-irtf-cfrg-bls-signature/)
with [these curve parameters](https://datatracker.ietf.org/doc/draft-irtf-cfrg-pairing-friendly-curves/)
for BLS12-381.

Features:

* Non-interactive signature aggregation following IETF specification
* Efficient verification using Proof of Possession (only one pairing per distinct message)
* Aggregate public keys and private keys
* [EIP-2333](https://eips.ethereum.org/EIPS/eip-2333) key derivation (including unhardened BIP-32-like keys)
* Key and signature serialization
* Batch verification

## Before you start

This library uses minimum public key sizes (MPL). A G2Element is a signature (96 bytes), and a G1Element is a public key (48 bytes). A private key is a 32 byte integer. There are three schemes: Basic, Augmented, and ProofOfPossession. Augmented should be enough for most use cases, and ProofOfPossession can be used where verification must be fast.

## Import the library

Coming soon.

## Creating keys and signatures

Coming soon.

## Serializing keys and signatures to bytes

Coming soon.

## Loading keys and signatures from bytes

Coming soon.

## Create aggregate signatures

Coming soon.

## Arbitrary trees of aggregates

Coming soon.

## Very fast verification with Proof of Possession scheme

Coming soon.

## HD keys using [EIP-2333](https://github.com/ethereum/EIPs/pull/2333)

Coming soon.

## Build

Performance times coming soon.

### Import the library to use it

Coming soon. 

## Contributing and workflow

Make a pull request.

## Specification and test vectors

The [IETF bls draft](https://datatracker.ietf.org/doc/draft-irtf-cfrg-hash-to-curve/)
is followed. Test vectors can also be seen in the python and cpp test files.

## Relic license

Relic is used with the
[Apache 2.0 license](https://github.com/relic-toolkit/relic/blob/master/LICENSE.Apache-2.0)