import time
import hashlib

class Block:
    def __init__(self, index, data, previous_hash, difficulty, nonce=0):
        """
        Constructor for the `Block` class.

        :param index: Unique ID of the block.
        :param data: Data stored in the block.
        :param previous_hash: Hash of the previous block in the chain.
        :param difficulty: The difficulty level for mining the block.
        :param nonce: The nonce used in the mining process.
        """
        self.index = index
        self.data = data
        self.timestamp = time.time()
        self.previous_hash = previous_hash
        self.difficulty = difficulty
        self.nonce = nonce
        self.hash = self.calculate_hash()

    def calculate_hash(self):
        """
        Calculate the hash of the block using SHA-256.

        :return: The calculated hash.
        """
        block_string = "{}{}{}{}{}{}".format(self.index, self.data, self.timestamp, self.previous_hash, self.difficulty, self.nonce)
        return hashlib.sha256(block_string.encode()).hexdigest()

    def __str__(self):
        """
        String representation of the block for easier debugging.

        :return: A string representation of the block.
        """
        return "Block(Index: {}, Data: {}, Timestamp: {}, Previous Hash: {}, Difficulty: {}, Nonce: {}, Hash: {})".format(
            self.index, self.data, self.timestamp, self.previous_hash, self.difficulty, self.nonce, self.hash)
