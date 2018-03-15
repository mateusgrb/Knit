package com.omerozer.knit;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;

import com.omerozer.knit.viewevents.GenericEvent;
import com.omerozer.knit.viewevents.GenericEventPool;
import com.omerozer.knit.viewevents.KnitOnClickEvent;
import com.omerozer.knit.viewevents.KnitOnClickEventPool;
import com.omerozer.knit.viewevents.KnitOnFocusChangedEvent;
import com.omerozer.knit.viewevents.KnitOnFocusChangedEventPool;
import com.omerozer.knit.viewevents.KnitOnRefreshEvent;
import com.omerozer.knit.viewevents.KnitOnSwitchToggleEvent;
import com.omerozer.knit.viewevents.KnitOnSwitchToggleEventPool;
import com.omerozer.knit.viewevents.KnitOnTextChangedEventPool;
import com.omerozer.knit.viewevents.KnitSwipeRefreshLayoutEventPool;
import com.omerozer.knit.viewevents.KnitTextChangedEvent;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by omerozer on 2/2/18.
 */

public class KnitEvents {

    private static Knit knitInstance;

    static void init(Knit knit){
        knitInstance = knit;
    }

    private static Map<View,Set<Object>> viewToListenersMap = new LinkedHashMap<>();

    private final static KnitOnClickEventPool onClickEventPool = new KnitOnClickEventPool();
    private final static KnitOnTextChangedEventPool onTextChangedEventPool = new KnitOnTextChangedEventPool();
    private final static KnitOnFocusChangedEventPool onFocusChangedEventPool = new KnitOnFocusChangedEventPool();
    private final static KnitSwipeRefreshLayoutEventPool onSwipeRefreshEventPool = new KnitSwipeRefreshLayoutEventPool();
    private final static KnitOnSwitchToggleEventPool onSwitchToggleEventPool = new KnitOnSwitchToggleEventPool();
    private final static GenericEventPool genericEventPool = new GenericEventPool();

    public static void onClick(final String tag, final Object carrierObject, View view) {
        view.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View view) {
                KnitOnClickEvent event = onClickEventPool.getEvent();
                event.setTag(tag);
                event.setViewWeakReference(view);
                knitInstance.findPresenterForView(carrierObject).handle(onClickEventPool, event, knitInstance.getModelManager());
            }
        });
    }

    public static void onTextChanged(final String tag, final Object carrierObject,
            final EditText view) {
        final TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                view.removeTextChangedListener(this);
                KnitTextChangedEvent event = onTextChangedEventPool.getEvent();
                event.setTag(tag);
                event.setState(KnitTextChangedEvent.State.BEFORE);
                event.setCharSequence(charSequence);
                event.setI(i);
                event.setI1(i1);
                event.setI2(i2);
                knitInstance.findPresenterForView(carrierObject).handle(onTextChangedEventPool, event,
                        knitInstance.getModelManager());
                view.addTextChangedListener(this);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                view.removeTextChangedListener(this);
                KnitTextChangedEvent event = onTextChangedEventPool.getEvent();
                event.setTag(tag);
                event.setState(KnitTextChangedEvent.State.ON);
                event.setCharSequence(charSequence);
                event.setI(i);
                event.setI1(i1);
                event.setI2(i2);
                knitInstance.findPresenterForView(carrierObject).handle(onTextChangedEventPool, event,
                        knitInstance.getModelManager());
                view.addTextChangedListener(this);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                view.removeTextChangedListener(this);
                KnitTextChangedEvent event = onTextChangedEventPool.getEvent();
                event.setTag(tag);
                event.setState(KnitTextChangedEvent.State.AFTER);
                event.setAfterEditable(editable);
                knitInstance.findPresenterForView(carrierObject).handle(onTextChangedEventPool, event,
                        knitInstance.getModelManager());
                view.addTextChangedListener(this);
            }
        };
        view.addTextChangedListener(watcher);
    }

    public static void onFocusChanged(final String tag, final Object carrierObject,
            final View view) {
        view.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                KnitOnFocusChangedEvent event = onFocusChangedEventPool.getEvent();
                event.setTag(tag);
                event.setFocus(b);
                knitInstance.findPresenterForView(carrierObject).handle(onFocusChangedEventPool, event, knitInstance.getModelManager());
            }
        });
    }

    public static void onSwipeRefresh(final String tag, final Object carrierObject,final SwipeRefreshLayout view) {
        view.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                KnitOnRefreshEvent event = onSwipeRefreshEventPool.getEvent();
                event.setTag(tag);
                event.setViewWeakReference(view);
                knitInstance.findPresenterForView(carrierObject).handle(onSwipeRefreshEventPool, event, knitInstance.getModelManager());
            }
        });
    }

    public static void onSwitchToggled(final String tag, final Object carrierObject,final Switch view){
        view.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                KnitOnSwitchToggleEvent event = onSwitchToggleEventPool.getEvent();
                event.setTag(tag);
                event.setToggle(isChecked);
                knitInstance.findPresenterForView(carrierObject).handle(onSwitchToggleEventPool,event,knitInstance.getModelManager());
            }
        });
    }

    public static<T> void fireGenericEvent(String tag,Object carrierObject,Object... params){
        GenericEvent genericEvent = genericEventPool.getEvent();
        genericEvent.setTag(tag);
        genericEvent.setParams(params);
        knitInstance.findPresenterForView(carrierObject).handle(genericEventPool, genericEvent, knitInstance.getModelManager());
    }


    public static void onViewResult(Object carrierObject,int requestCode,int resultCode,Intent data){
        knitInstance.findPresenterForView(carrierObject).onViewResult(requestCode,resultCode,data);
    }


}
