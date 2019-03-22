package com.google.appinventor.components.runtime;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.google.appinventor.components.annotations.DesignerComponent;
import com.google.appinventor.components.annotations.DesignerProperty;
import com.google.appinventor.components.annotations.PropertyCategory;
import com.google.appinventor.components.annotations.SimpleEvent;
import com.google.appinventor.components.annotations.SimpleFunction;
import com.google.appinventor.components.annotations.SimpleObject;
import com.google.appinventor.components.annotations.SimpleProperty;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.runtime.util.ElementsUtil;
import com.google.appinventor.components.runtime.util.YailList;

@SimpleObject
@DesignerComponent(category = ComponentCategory.USERINTERFACE, description = "<p>A spinner component that displays a pop-up with a list of elements. These elements can be set in the Designer or Blocks Editor by setting the<code>ElementsFromString</code> property to a string-separated concatenation (for example, <em>choice 1, choice 2, choice 3</em>) or by setting the <code>Elements</code> property to a List in the Blocks editor. Spinners are created with the first item already selected. So selecting  it does not generate an After Picking event. Consequently it's useful to make the  first Spinner item be a non-choice like \"Select from below...\". </p>", docUri = "user-interface/listpicker", iconName = "images/spinner.png", nonVisible = false, version = 2)
public final class Spinner extends AndroidViewComponent implements OnItemSelectedListener {
    public static final int DEFAULT_MODE = 1;
    private ArrayAdapter<String> adapter;
    private YailList items = new YailList();
    private int mode;
    private int oldAdapterCount;
    private int oldSelectionIndex;
    private android.widget.Spinner view;

    public Spinner(ComponentContainer container) {
        super(container);
        SpinnerMode(1);
        Prompt("");
        this.oldSelectionIndex = SelectionIndex();
    }

    @DesignerProperty(defaultValue = "1", editorType = "spinner_mode")
    @SimpleProperty(category = PropertyCategory.APPEARANCE, description = "Decide that Spinner display the list with a dropdown or a dialog")
    public void SpinnerMode(int mode) {
        int i = 1;
        Context $context = this.container.$context();
        if (mode == 0 || mode == 1) {
            i = mode;
        }
        android.widget.Spinner spinner = new android.widget.Spinner($context, i);
        this.adapter = new ArrayAdapter(this.container.$context(), 17367048);
        this.adapter.setDropDownViewResource(17367058);
        spinner.setAdapter(this.adapter);
        spinner.setOnItemSelectedListener(this);
        if (this.view != null) {
            ViewGroup vg = (ViewGroup) this.view.getParent();
            int index = vg.indexOfChild(this.view);
            int selected = SelectionIndex();
            String prompt = Prompt();
            vg.removeView(this.view);
            this.view = spinner;
            vg.addView(this.view, index, LinearLayout.defaultLayoutParams());
            setAdapterData(this.items.toStringArray());
            SelectionIndex(selected);
            Prompt(prompt);
        } else {
            this.view = spinner;
            this.container.$add(this);
        }
        this.mode = mode;
    }

    @SimpleProperty(description = "Return mode value which decides whether the Spinner displays the list with a dropdown or a dialog")
    public int SpinnerMode() {
        return this.mode;
    }

    public View getView() {
        return this.view;
    }

    @SimpleProperty(category = PropertyCategory.BEHAVIOR, description = "Returns the current selected item in the spinner ")
    public String Selection() {
        return SelectionIndex() == 0 ? "" : (String) this.view.getItemAtPosition(SelectionIndex() - 1);
    }

    @DesignerProperty(defaultValue = "", editorType = "string")
    @SimpleProperty(category = PropertyCategory.BEHAVIOR, description = "Set the selected item in the spinner")
    public void Selection(String value) {
        SelectionIndex(ElementsUtil.setSelectedIndexFromValue(value, this.items));
    }

    @SimpleProperty(category = PropertyCategory.BEHAVIOR, description = "The index of the currently selected item, starting at 1. If no item is selected, the value will be 0.")
    public int SelectionIndex() {
        return ElementsUtil.selectionIndex(this.view.getSelectedItemPosition() + 1, this.items);
    }

    @SimpleProperty(category = PropertyCategory.BEHAVIOR, description = "Set the spinner selection to the element at the given index.If an attempt is made to set this to a number less than 1 or greater than the number of items in the Spinner, SelectionIndex will be set to 0, and Selection will be set to empty.")
    public void SelectionIndex(int index) {
        this.oldSelectionIndex = SelectionIndex();
        this.view.setSelection(ElementsUtil.selectionIndex(index, this.items) - 1);
        TextView spinnerText = (TextView) this.view.getChildAt(0);
        if (spinnerText != null) {
            spinnerText.setTextColor(-16777216);
        }
    }

    @SimpleProperty(category = PropertyCategory.BEHAVIOR, description = "returns a list of text elements to be picked from.")
    public YailList Elements() {
        return this.items;
    }

    @SimpleProperty(category = PropertyCategory.BEHAVIOR, description = "adds the passed text element to the Spinner list")
    public void Elements(YailList itemList) {
        if (itemList.size() == 0) {
            SelectionIndex(0);
        } else if (itemList.size() < this.items.size() && SelectionIndex() == this.items.size()) {
            SelectionIndex(itemList.size());
        }
        this.items = ElementsUtil.elements(itemList, "Spinner");
        setAdapterData(itemList.toStringArray());
    }

    @DesignerProperty(defaultValue = "", editorType = "string")
    @SimpleProperty(category = PropertyCategory.BEHAVIOR, description = "sets the Spinner list to the elements passed in the comma-separated string")
    public void ElementsFromString(String itemstring) {
        Elements(ElementsUtil.elementsFromString(itemstring));
    }

    private void setAdapterData(String[] theItems) {
        this.oldAdapterCount = this.adapter.getCount();
        this.adapter.clear();
        for (Object add : theItems) {
            this.adapter.add(add);
        }
    }

    @SimpleProperty(category = PropertyCategory.APPEARANCE, description = "Text with the current title for the Spinner window. Only meaningful when mode is DIALOG")
    public String Prompt() {
        return this.view.getPrompt().toString();
    }

    @DesignerProperty(defaultValue = "", editorType = "string")
    @SimpleProperty(category = PropertyCategory.APPEARANCE, description = "sets the Spinner window prompt to the given title. Only meaningful when mode is DIALOG")
    public void Prompt(String str) {
        this.view.setPrompt(str);
    }

    @SimpleFunction(description = "displays the dropdown list for selection, same action as when the user clicks on the spinner.")
    public void DisplayDropdown() {
        this.view.performClick();
    }

    @SimpleEvent(description = "Event called after the user selects an item from the dropdown list.")
    public void AfterSelecting(String selection) {
        EventDispatcher.dispatchEvent(this, "AfterSelecting", selection);
    }

    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
        if (!(this.oldAdapterCount == 0 && this.adapter.getCount() > 0 && this.oldSelectionIndex == 0) && (this.oldAdapterCount <= this.adapter.getCount() || this.oldSelectionIndex <= this.adapter.getCount())) {
            SelectionIndex(position + 1);
            AfterSelecting(Selection());
            return;
        }
        SelectionIndex(position + 1);
        this.oldAdapterCount = this.adapter.getCount();
    }

    public void onNothingSelected(AdapterView<?> adapterView) {
        this.view.setSelection(0);
    }
}
