package com.example.demo.controller.editor;

import java.beans.*;
import java.time.*;

public class MyDatePropertyEditor extends PropertyEditorSupport {
	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		// 2020-11-10
		String[] arr = text.split("-");
		Integer year = Integer.parseInt(arr[0]);
		Integer month = Integer.parseInt(arr[1]);
		Integer day = Integer.parseInt(arr[2]);
		setValue(LocalDate.of(year, month, day));
	}
}
