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
import java.io.*;

public class Blender {
	private String buffer = "";
	private ModelParser2 modelparser2;

	private int[] xarray;
	private int[] yarray;
	private int[] zarray;
	private int[] carray;

	public Blender(String blenderfilename) {

		readinfile(blenderfilename);
		char b[] = new char[buffer.length()*10];//FIXME length
		int i = 0;
		while ( i < buffer.length()) {

			b[i] = buffer.charAt(i); 

			i++;
		}
		modelparser2 = new ModelParser2(b);	

		xarray = modelparser2.getxarray(); //returns all x coords in order
		yarray = modelparser2.getyarray(); //returns all x coords in order
		zarray = modelparser2.getzarray(); //returns all x coords in order
		carray = modelparser2.getcarray(); //returns all x coords in order


	}

	public int[] getcarray()
	{
		return carray;
	}

	public int[] getxarray()
	{
		return xarray;
	}

	public int[] getyarray()
	{
		return yarray;
	}

	public int[] getzarray()
	{
		return zarray;
	}


/***
	public static void main(String[] args)
	{
		if (args == null) {
			System.out.println("usage: java javablender/Blender <blendfilename>");
			System.exit(0);
		}
		new Blender(args[0]);
	}
***/	public void readinfile(String filename) {
	
	try {
		FileReader input  = new FileReader(filename);



		BufferedReader bufread = new BufferedReader(input);

		String line  = bufread.readLine();
		
		while (line != null) {
			buffer += line;
			buffer += "\n";
			line = bufread.readLine();	
		}

		buffer += "\n";
		bufread.close();

	}catch (IOException e) {
	}
	}


};
