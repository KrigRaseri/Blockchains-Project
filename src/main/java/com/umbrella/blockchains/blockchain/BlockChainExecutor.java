package com.umbrella.blockchains.blockchain;

import com.google.inject.Inject;
import com.umbrella.blockchains.messenger.Message;
import com.umbrella.blockchains.messenger.MessengerExceptions;
import com.umbrella.blockchains.messenger.MessengerUtil;
import com.umbrella.blockchains.messenger.Receiver;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.concurrent.TimeUnit.SECONDS;

@AllArgsConstructor(onConstructor = @__({@Inject}))
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BlockChainExecutor {
    private BlockMiner blockMiner;

    private final ArrayBlockingQueue<Runnable> boundedQueue = new ArrayBlockingQueue<>(1000);
    protected final ExecutorService executorService = new ThreadPoolExecutor(10, 20,
            30, SECONDS,
            boundedQueue,
            new ThreadPoolExecutor.AbortPolicy());

    /**
     * Creates a list of Callable objects to generate proved blocks.
     *
     * @param block        The previous block in the blockchain.
     * @param leadingZeros The desired number of leading zeros.
     * @return The list of Callable objects.
     */
    protected List<Callable<Block>> createCallableList(Block block, String leadingZeros) {
        return IntStream.range(0, 20)
                .mapToObj(j -> newCallable(block, leadingZeros))
                .collect(Collectors.toList());
    }

    /**
     * Creates a new Callable object for generating proved blocks.
     *
     * @param block        The previous block in the blockchain.
     * @param leadingZeros The desired number of leading zeros.
     * @return The Callable object.
     */
    protected Callable<Block> newCallable(Block block, String leadingZeros) {
        return () -> blockMiner.mineProvedBlock(block, leadingZeros);
    }

    /**
     * Starts a task to collect messages concurrently until the stop flag is set. Each message and pulled from a small
     * random list of pre-made messages. The message is encrypted and signed with a signature made from a private key.
     * Then the message is decrypted and validated based on the public key, if true it is added to the recorded messages
     * list. Note: This is only for test and demo purposes.
     *
     * @param stopFlag The AtomicBoolean flag to stop the message collection.
     * @return The Future object representing the task's result.
     */
    protected Future<List<String>> startMessageCollectionTask(AtomicBoolean stopFlag) {
        String publicKeyPath = "./src/main/resources/KeyPair/publicKey";
        String privateKeyPath = "./src/main/resources/KeyPair/privateKey";
        String signedDataPath = "./src/main/resources/MyData/SignedData.txt";
        Receiver receiver = new Receiver();

        return executorService.submit(() -> {
            List<String> recordedMessages = new ArrayList<>();
            while (!stopFlag.get()) {
                Message message = new Message(MessengerUtil.getRandomMessage(), privateKeyPath);
                message.writeToFile(signedDataPath);
                String storedMessage = receiver.decryptMessage(signedDataPath, publicKeyPath);
                recordedMessages.add(storedMessage);
                Thread.sleep(100);
            }
            return recordedMessages;
        });
    }
}
