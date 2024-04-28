package com.example.uniqueidgenerator.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.*;

@Slf4j
public class UniqueIdGeneratorTest {

    private UniqueIdGenerator idGenerator;

    @BeforeEach
    public void setup() {
        this.idGenerator = new UniqueIdGenerator(1, 1);
    }

    @Test
    public void shouldGenerateAllUniqueIds() {
        Set<Long> ids = new HashSet<>();

        int numOfTests = 1000;

        long startTime = System.nanoTime();
        for(int i=0; i<numOfTests; i++) {
            ids.add(this.idGenerator.generateId().getId());
        }
        long endTime = System.nanoTime();

        log.info("time taken (ns) to generate {} ids: {}", numOfTests, endTime-startTime);
        Assertions.assertEquals(ids.size(), numOfTests);
    }

    /**
     * ids generated within a millisecond should have same epoch part.
     * For two ids generated within same millisecond, the seq number
     * of id generated after should have seq number greater than the
     * seq number of id generated before
     */
    @Test
    public void epochValTest1() throws ExecutionException, InterruptedException, TimeoutException {

        ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

        long delayTo10thMillisecondFromCurrent = (1000000 - (System.nanoTime()%1000000) ) + 10000000;

        ScheduledFuture<?> futureResult =  scheduledExecutorService.schedule(() -> {
            long startTime = System.nanoTime();
            long id1 = this.idGenerator.generateId().getId();
            long id2 = this.idGenerator.generateId().getId();
            long endTime = System.nanoTime();

            log.info("id1: {}; id2: {}", id1, id2);
            log.info("time taken (ns) to generate ids: {}", endTime - startTime);

            long epochPartId1 = extractEpoch(id1);
            long epochPartId2 = extractEpoch(id2);

            log.info("epoch1: {}; epoch2: {}", epochPartId1, epochPartId2);

            Assertions.assertEquals(epochPartId1, epochPartId2);

            long seqNumId1 = extractSeqNum(id1);
            long seqNumId2 = extractSeqNum(id2);

            log.info("seqNum1: {}; seqNum1: {}", seqNumId1, seqNumId2);

            Assertions.assertTrue(seqNumId1 < seqNumId2);
        }, delayTo10thMillisecondFromCurrent, TimeUnit.NANOSECONDS);

        futureResult.get(5, TimeUnit.SECONDS);
    }

    /**
     * ids that were generated more than a millisecond
     * apart should have different epoch times
     */
    @Test
    public void epochValuesTest2() throws InterruptedException {
        long id1 = this.idGenerator.generateId().getId();

        Thread.sleep(1);

        long id2 = this.idGenerator.generateId().getId();

        long epochPartId1 = extractEpoch(id1);
        long epochPartId2 = extractEpoch(id2);

        log.info("epoch1: {}; epoch2: {}",epochPartId1, epochPartId2);

        Assertions.assertNotEquals(epochPartId1, epochPartId2);
    }

    private long extractEpoch(long id) {
        return ( id  >> 22);
    }

    private long extractSeqNum(long id) {
        return (id & 4095L);
    }
}
