package com.example.uniqueidgenerator.service;

import com.example.uniqueidgenerator.model.AppPlacement;
import com.example.uniqueidgenerator.model.UniqueId;
import lombok.extern.slf4j.Slf4j;
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

    public UniqueIdGenerator(AppPlacement appPlacement) {
        this.dcBits = (appPlacement.getDatacenterNum() & 31L ) << 17;
        this.workerNodeBits = (appPlacement.getPodOrdinal() & 31L) << 12;
        this.prevEpochMillis = Instant.now().toEpochMilli();
        this.seqNumCurrentVal = 0;
        this.seqNumMask = 4095L;
        this.epochMillisMask = 2199023255551L;
    }

    /**
     * @return id
     * method to generate unique ids. only one thread can
     * generate unique id at a time.
     *
     * id structure -
     * 1 bit-----41 bits---------------5 bits------------5 bits------------12 bits
     * 0        epoch in millis     datacenter id       machineid       seq num auto reset every millisec
     */
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
