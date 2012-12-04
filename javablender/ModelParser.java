package javablender;
/*
*
* Copyright 2012 Johan Ceuppens 
*
* This library is free software; you can redistribute it and/or
* modify it under the terms of the GNU Lesser General Public
* License as published by the Free Software Foundation; either
* version 2.0 of the License, or (at your option) any later version.
*
* This library is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
* Lesser General Public License for more details.
*
* You should have received a copy of the GNU Lesser General Public
* License along with this library. If not, see <http://www.gnu.org/licenses/>.
*
*/

import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ModelParser {
	private int bufindex = 0;

	private int pointersize = 32;
	private String endianess = "";

	public ModelParser(String buf) {
		if (parseheader(buf)==0)
			if (parsepointersize(buf)==0)
				if (parseendianess(buf)==0)
					return;	
	

	}

	public int parseendianess(String buf)
	{
		char endianesschar = buf.charAt(bufindex);
		if (endianesschar == 'v') 
			endianess = "little";	
		else if (endianesschar == 'v') 
			endianess = "big";	
		bufindex += 1;
		return 0;
	}
	public int parseversion(String buf)
	{
		int majorversion = buf.charAt(bufindex+0);
		int minorversion = buf.charAt(bufindex+1);
		int minorversion2 = buf.charAt(bufindex+2);
		bufindex += 3;
		return 0;	
	}
	public int parsepointersize(String buf)
	{

		char pointersizechar = buf.charAt(bufindex);
		bufindex += 1;
		if (pointersizechar == '_') {
			pointersize = 32;
			return 0;
		}
		else if (pointersizechar == '-') {
			pointersize = 64;	
			return 0;
		}		
	
		return -1;
	}
	public int parseheader(String buf)
	{

		char b = buf.charAt(bufindex+0);	
		char l = buf.charAt(bufindex+1);	
		char e = buf.charAt(bufindex+2);	
		char n = buf.charAt(bufindex+3);	
		char d = buf.charAt(bufindex+4);	
		char e2 = buf.charAt(bufindex+5);	
		char r = buf.charAt(bufindex+6);	

		bufindex += 6;	
		if (""+b+l+e+n+d+e2+r == "BLENDER")
			return 0;
		else 
			return -1;
	}
};
