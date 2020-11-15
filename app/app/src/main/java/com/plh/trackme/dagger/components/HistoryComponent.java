package com.plh.trackme.dagger.components;

import com.plh.trackme.dagger.modules.HistoryModule;
import com.plh.trackme.dagger.modules.RecordModule;
import com.plh.trackme.dagger.scopes.FragmentScope;
import com.plh.trackme.fragment.HistoryFragment;
import com.plh.trackme.fragment.RecordFragment;

import dagger.Subcomponent;

@FragmentScope
@Subcomponent(modules = {
        HistoryModule.class
})

public interface HistoryComponent {

    void inject(HistoryFragment fragment);
}
