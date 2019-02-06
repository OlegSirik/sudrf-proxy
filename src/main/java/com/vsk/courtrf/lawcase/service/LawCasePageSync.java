package com.vsk.courtrf.lawcase.service;

import com.vsk.courtrf.lawcase.entity.LawCase;
import com.vsk.courtrf.lawcase.entity.LawCaseSearchReq;

public interface LawCasePageSync {
    void addLawCase4SyncDetails(LawCase lawCase);
    void addData4SyncList( LawCaseSearchReq req);

    boolean getRunningSudrf();
    boolean getRunningMossud();
    boolean getRunningArbitr();

    void startSyncSudrf ();
    void startSyncMossud();
    void startSyncArbitr();
}
