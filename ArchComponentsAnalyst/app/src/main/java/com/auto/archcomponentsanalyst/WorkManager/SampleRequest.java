package com.auto.archcomponentsanalyst.WorkManager;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;

import java.util.Set;
import java.util.UUID;

import androidx.work.WorkRequest;
import androidx.work.impl.model.WorkSpec;

/**
 * Created by haohuidong on 18-6-25.
 */

public class SampleRequest extends WorkRequest {
    /**
     * @param id
     * @param workSpec
     * @param tags
     * @hide
     */
    @SuppressLint("RestrictedApi")
    protected SampleRequest(@NonNull UUID id, @NonNull WorkSpec workSpec, @NonNull Set<String> tags) {
        super(id, workSpec, tags);
    }
}
