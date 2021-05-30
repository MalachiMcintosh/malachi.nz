package com.malachi;

/**
 * CV Page Counter
 */
public class Counter {
    // {"id":"count","_rid":"qw0ZAJjT2FcBAAAAAAAAAA==","_self":"dbs/qw0ZAA==/colls/qw0ZAJjT2Fc=/docs/qw0ZAJjT2FcBAAAAAAAAAA==/","_ts":1619597514,"_etag":"\"7300021e-0000-1a00-0000-608918ca0000\"","currentCount":1,"isComplete":false}
    private String id;
    private String _rid;
    private String _self;
    private Double _ts;
    private String _etag;
    private Integer currentCount;
    private Boolean isComplete;

    // public Counter(){
    //     this.currentCount = 0;
    //     this.id = "count";
    //     this.isComplete = false;
    // }

    public Counter(){
    }

    public Integer getCount(){
        return this.currentCount;
    }

    public String getCountString(){
        return this.currentCount.toString();
    }

    public void increment(){
        this.currentCount = getCount().intValue()+1;
    }

    @Override
    public String toString(){
        return "Count is "+currentCount.toString();
    }

}
