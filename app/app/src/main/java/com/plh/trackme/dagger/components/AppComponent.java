package com.plh.trackme.dagger.components;

import com.plh.trackme.dagger.modules.HistoryModule;
import com.plh.trackme.dagger.modules.RecordModule;

import javax.inject.Singleton;

import dagger.Component;

@Component(modules = {})
@Singleton
public interface AppComponent {

    RecordComponent plus(RecordModule recordModule);

    HistoryComponent plus(HistoryModule historyModule);
}
