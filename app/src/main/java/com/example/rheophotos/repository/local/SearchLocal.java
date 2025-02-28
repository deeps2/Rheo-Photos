package com.example.rheophotos.repository.local;

import com.example.rheophotos.utils.ExecutorUtils;

import java.util.concurrent.atomic.AtomicInteger;

public class SearchLocal {
    private static SearchLocal sInstance;
    private AtomicInteger totalRows = new AtomicInteger(0);

    private SearchLocal() {
    }

    public static synchronized SearchLocal getInstance() {
        if (sInstance == null)
            sInstance = new SearchLocal();
        return sInstance;
    }

    public int getTotalRows() {
        return totalRows.get();
    }

    //increase total row count by 1 when inserting a new record in table
    public void incrementTotalRows() {
        this.totalRows.incrementAndGet();
    }

    //decrease row count by 1 when deleting a new record in table
    public void decrementTotalRows() {
        this.totalRows.decrementAndGet();
    }

    //get total rows from SearchTable.
    //This is done so that before inserting we don't have to query the database again to get its size
    //DB can have max SearchTable.MAX_ROWS otherwise cache size of app will keep on increasing with new search results
    //After reaching the threshold size of MAX_ROWS we will delete the oldest record in DB and then insert new record
    public void getTotalRowsFromDB() {
        ExecutorUtils.getInstance().diskIO().execute(() -> {
            int count = RheoRoomDBHelper.getInstance().searchDao().getTotalRows();
            totalRows.set(count);
        });
    }
}
