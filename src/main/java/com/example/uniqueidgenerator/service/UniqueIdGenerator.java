package com.example.uniqueidgenerator.service;

import com.example.uniqueidgenerator.model.UniqueId;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@Slf4j
public class UniqueIdGenerator {

    private long prevEpochMillis;

    private long seqNumCurrentVal;

    private long dcBits;

    private long workerNodeBits;

    private long seqNumMask;

    private long epochMillisMask;

    public UniqueIdGenerator(@Value("${app.placement.datacenter-num}") int datacenterNum,
                             @Value("${app.placement.workernode-num}") int workerNodeNum
    ) {
        this.dcBits = (datacenterNum & 31L ) << 17;
        this.workerNodeBits = (workerNodeNum & 31L) << 12;
        this.prevEpochMillis = Instant.now().toEpochMilli();
        this.seqNumCurrentVal = 0;
        this.seqNumMask = 4095L;
        this.epochMillisMask = 2199023255551L;
    }

    public synchronized UniqueId generateId() {
        //long startTime = System.nanoTime();

        long currentEpochMillis = Instant.now().toEpochMilli();

        if (currentEpochMillis > prevEpochMillis) {
            this.prevEpochMillis = currentEpochMillis;
            this.seqNumCurrentVal = 0;
        } else if (currentEpochMillis == prevEpochMillis) {
            this.seqNumCurrentVal = this.seqNumCurrentVal + 1;
        }

        long currentEpochMillisBits = (currentEpochMillis & this.epochMillisMask) << 22;

        long seqNumBits = ( this.seqNumCurrentVal & this.seqNumMask );

        long id = (currentEpochMillisBits | this.dcBits | this.workerNodeBits | seqNumBits );

        //long endTime = System.nanoTime();

        //log.info("id generation time in nano secs: {}", (endTime - startTime));
        return new UniqueId(id);
    }

}
