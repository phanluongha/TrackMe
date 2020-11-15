package com.plh.trackme.dagger.components;

import com.plh.trackme.dagger.modules.RecordModule;
import com.plh.trackme.dagger.scopes.FragmentScope;
import com.plh.trackme.fragment.RecordFragment;

import dagger.Subcomponent;

@FragmentScope
@Subcomponent(modules = {
        RecordModule.class
})

public interface RecordComponent {

    void inject(RecordFragment fragment);
}
