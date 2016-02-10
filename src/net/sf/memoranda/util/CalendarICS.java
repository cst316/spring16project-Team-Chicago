package net.sf.memoranda.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import javax.xml.transform.TransformerException;

import biweekly.*;
import biweekly.component.VEvent;
import biweekly.util.ICalDate;

public class CalendarICS {

	public static void importCalendar(File f){
		try {
			FileInputStream file = new FileInputStream(f);
			List<ICalendar> icals = Biweekly.parse(file).all();
			int eventCount = icals.get(0).getEvents().size();
			for(int i=0;i<eventCount;i++){
				VEvent event = icals.get(0).getEvents().get(i);
				//String summary = event.getSummary().getValue();
				//System.out.println(summary);
			} 
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
