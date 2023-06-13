package com.umbrella.blockchains.blockchain;

import com.google.inject.Inject;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.umbrella.blockchains.blockchain.BlockChainUtil.getRandomMessage;
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
     * Starts a task to collect messages concurrently until the stop flag is set.
     *
     * @param stopFlag The AtomicBoolean flag to stop the message collection.
     * @return The Future object representing the task's result.
     */
    protected Future<List<String>> startMessageCollectionTask(AtomicBoolean stopFlag) {
        return executorService.submit(() -> {
            List<String> recordedMessages = new ArrayList<>();
            while (!stopFlag.get()) {
                recordedMessages.add(getRandomMessage());
                Thread.sleep(100);
            }
            return recordedMessages;
        });
    }
}
