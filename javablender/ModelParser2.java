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

public class ModelParser2 {
	private int countstructure = 0;
	private int SDNA_index = 0;
	private int pointersize = 32;
	private String endianess = "";

	private int[] xs;
	private int[] ys;
	private int[] zs;
	private int[] cs;
	private String fileblockid = "";
	//buf is the file contents
	public ModelParser2(char[] buf) {
		int index = 0;
		if (parseheader(buf,index)<=0) {
			if (parsepointersize(buf,index)<=0) {
				if (parseendianess(buf,index)<=0)
					if (parseversion(buf,index) <= 0)
					 {
						for (; index < buf.length; ) { 
							if (parsefileblock(buf,index) < 0) {
				
								System.out.println("123> EOF");							
								return ;	
						}		
					}
				}
				else System.out.println("123> no endianess");	
			}else System.out.println("123> no pointer size");	
		}else System.out.println("123> no header");	

	}

	public void padbytes(int index) 
	{

		index -= index%4;

	}
	
	public int[] getcarray()
	{
		return cs;
	}

	public int[] getxarray()
	{
		return xs;
	}

	public int[] getyarray()
	{
		return ys;
	}

	public int[] getzarray()
	{
		return zs;
	}


/*
 * parsing file header
 */ 
	public int parseendianess(char[] buf, int index)
	{
		char endianesschar = (char)buf[index];
		if (endianesschar == 'v') 
			endianess = "little";	
		else if (endianesschar == 'V') 
			endianess = "big";	
		index += 1;

		return 0;
	}

	public int parsespace(char[] buf,int index)
	{
		for (;buf[index++] != 32;)
			;

		return 0;
	}

	public int parseversion(char[] buf, int index)
	{
		char majorversion = (char)buf[index+0];
		char minorversion = (char)buf[index+1];
		char minorversion2 = (char)buf[index+2];
		index += 3;


		return 0;	
	}
	public int parsepointersize(char[] buf,int index)
	{

		char pointersizechar = (char)buf[index];
		index += 1;
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
	public int parseheader(char[] buf,int index)
	{

		char b = (char)buf[index+0];	
		char l = (char)buf[index+1];	
		char e = (char)buf[index+2];	
		char n = (char)buf[index+3];	
		char d = (char)buf[index+4];	
		char e2 = (char)buf[index+5];	
		char r = (char)buf[index+6];	
		System.out.println(""+b+l+e+n+d+e2+r);

		index += 6;	
		if ((""+b+l+e+n+d+e2+r) == "BLENDER")
			return 0;
		else 
			return -1;
	}
/*
 * parsing file blocks 
 */ 

	public int parsescene(char[] buf, int index)
	{
		int startindex = index;
		while (index < buf.length) {//FIXME
			char c1 = buf[index];
			char c2 = buf[index+1];	
			char c3 = buf[index+2];	
			char c4 = buf[index+3];	
			char c5 = buf[index+4];	
			char c6 = buf[index+5];	
			if (c1 == 'S' && c2 == 'C') {
				System.out.println("block id> "+c1+c2);
				break;
			}	
			index += 2;	
		}
		return 0;
	}

	public int parsefileblock(char[] buf, int index) {

/****
		for (; index < buf.length-4; ) {

		char[] cs = new char[4];
		cs[0] = buf[index+0];
		cs[1] = buf[index+1];
		cs[2] = buf[index+2];
		cs[3] = buf[index+3];
		index += 2;
		if (cs[0] == 'D' && cs[1] == 'N' && cs[2] == 'A' && cs[3] == '1') {
			index -= 4;	
			break;
		}
		}	
***/		int size = parsefileblockheader(buf,index);
		if (fileblockid == "DNA1")
			parseDNA(buf,index);
		else
			parsefileblockdata(buf,index,size);
	
		return 0;	
	}

	public int parsefileblockdata(char[] buf, int index, int blocksize) {
		int i;
		for (i = 0; i < blocksize; i++) {
			char c = buf[index+i];	
		}
		index += i;
		return 0;
	}

	//code is "" if you want to parse thew next fileblock
	//code is e.g. "DNA1" for searching that fileblock id
	//it can be programmed better
	public int parsefileblockheader(char[] buf, int index)
	{

		char[] id = new char[4];//4 wide
		id[0] = (char)(buf[index]);
		id[1] = (char)(buf[index+1]);
		id[2] = (char)(buf[index+2]);
		id[3] = (char)(buf[index+3]);

		fileblockid = ""+id[0]+id[1]+id[2]+id[3];	

		System.out.println("fileblockid> "+id[0]+id[1]+id[2]+id[3]);
		index += 4;
		int size;
		if (endianess == "big")
			size = buf[index]*2*2*2+buf[index+1]*2*2+buf[index+2]*2+buf[index+3]*1;
		else
			size = buf[index+3]*2*2*2+buf[index+2]*2*2+buf[index+1]*2+buf[index]*1;
		System.out.println("size> "+size);
		index += 4;
		if (pointersize == 64) {
			index+=8;
		} else if (pointersize == 32) {
			index+=4;
		}
		if (endianess == "big")
			SDNA_index = buf[index]*2*2*2+buf[index+1]*2*2+buf[index+2]*2+buf[index+3];
		else
			SDNA_index = buf[index+3]*2*2*2+buf[index+2]*2*2+buf[index+1]*2+buf[index];

		System.out.println("SDNA_index> "+SDNA_index);
			index+=8;
		if (pointersize == 64) {
			index+=pointersize;
		} else if (pointersize == 32) {
			index+=pointersize;
		}
		index += 4;
		if (endianess == "big")
			countstructure = buf[index]*2*2*2+buf[index+1]*2*2+buf[index+2]*2+buf[index+3];
		else
			countstructure = buf[index+3]*2*2*2+buf[index+2]*2*2+buf[index+1]*2+buf[index];

		System.out.println("count> " + countstructure);

		index += 12;
		if (pointersize == 64) {
			index+=pointersize;
		} else if (pointersize == 32) {
			index+=pointersize;
		}

		return size;

	}

	public int parsevertices(char[] buf, int index)
	{
		while (index < buf.length) {
			char c1 = buf[index];
			char c2 = buf[index+1];	
			char c3 = buf[index+2];	
			char c4 = buf[index+3];	
			if (/*c1 == 'V' &&*/ c2 == 'e') {
				System.out.println("vertex id> "+c1+c2+c3+c4);
				break;
			}
			index += 2;	
			int i;
			for ( i = index; i < buf.length; i++) {
				if ((buf[i] >= 48 && buf[i] <= 57 && buf[i] >= 65 && buf[i] <= 90 && buf[i] >= 97 && buf[i] <= 122) || buf[i] == 95) {//FIXME
									
				}		
			}
			index += i;
			
	
		}
		return 0;
	}
	public int parseverticesnooffset(char[] buf, int index)
	{
		//padbytes(index);
		int j = 0;
		xs = new int[buf.length-index];//FIXME length	
		ys = new int[buf.length-index];	
		zs = new int[buf.length-index];	
		cs = new int[buf.length-index];	

		for (int i = index; j < countstructure; ) {//FIXME 17

			int xx = 0, yy = 0, zz = 0, cc = 0;

			if (buf[i] == 32 ) {//FIXME EOF
				i++;
				int k,l;

				for (k = i; buf[k] != 32; k++)
					; 
				
				for (l = i; l < k; l++) {	 
					xx += 10*(k-l)*(buf[l]-47);//convert ascii code
				}	
				i = l;		
					
			}
	
			else {
				i++;
				continue;
			}		

			if (buf[i] == 32 ) {//FIXME EOF
				i++;
				int k,l;

				for (k = i; buf[k] != 32; k++)
					; 
				
				for (l = i; l < k; l++) {	 
					yy += 10*(k-l)*(buf[l]-47);//convert ascii code
				}	
				i = l;		
					
			}
	
			else {
			}		
			if (buf[i] == 32 ) {//FIXME EOF
				i++;
				int k,l;

				for (k = i; buf[k] != 32; k++)
					; 
				
				for (l = i; l < k; l++) {	 
					zz += 10*(k-l)*(buf[l]-47);//convert ascii code
				}	
				i = l;		
					
			}
	
			else {
			}		
			if (buf[i] == 32 ) {//FIXME EOF
				i++;
				int k,l;

				for (k = i; buf[k] != 32; k++)
					; 
				
				for (l = i; l < k; l++) {	 
					cc += 10*(k-l)*(buf[l]-47);//convert ascii code
				}	
				i = l;		
					
			}
	
			else {
			}	

			System.out.println("x= "+xx+" y="+yy+" z="+zz+" c="+cc);	
				
			xs[j] = xx;
			ys[j] = yy;
			zs[j] = zz;
			cs[j] = cc;
			j++;
		}
		return 0;
	}

	public int parseDNA(char[] buf, int index)
	{
		char[] id = new char[8];
		id[0] = buf[index];
		id[1] = buf[index+1];
		id[2] = buf[index+2];
		id[3] = buf[index+3];
		id[4] = buf[index+4];
		id[5] = buf[index+5];
		id[6] = buf[index+6];
		id[7] = buf[index+7];
		System.out.println("DNAindex added, SC> "+id[0]+id[1]+id[2]+id[3]+id[4]+id[5]+id[6]+id[7]);
		//if (id[0] == 'D' && id[1] == 'N' && id[2] == 'A' && id[3] == '1') {

			index += 8;

			int namesn;

			if (endianess == "big")
				namesn = 2*2*2*buf[index]+2*2*buf[index+1]+2*buf[index+2]+1*buf[index+3];
			else
				namesn = 2*2*2*buf[index+3]+2*2*buf[index+2]+2*buf[index+1]+1*buf[index];
			System.out.println("namesn> "+namesn);
			for (int i = 0; i < namesn; i++) {
				int j;
				for (j = 0;;j++ ) {

					char c = buf[index+j];
					System.out.println("DNAname> "+c);
					if (c == '\0')
						break;	
				} 
				index += j+1;	
				index += 4;//'TYPE'
				
				int typesn;
				if (endianess == "big")
					typesn = 2*2*2*buf[index]+2*2*buf[index+1]+2*buf[index+2]+1*buf[index+3];
				else
					typesn = 2*2*2*buf[index+3]+2*2*buf[index+2]+2*buf[index+1]+1*buf[index];

				for (int k = 0; k < typesn; k++) {
					int o;
					for (o = 0;;o++ ) {

						char c = buf[index+o];
						System.out.println("TYPEname> "+c);
						if (c == '\0')
							break;	
					} 
					index += o+1;	

					index +=2;
					index += 4;//length id
				}	
				for (int k = 0; k < typesn; k++) {

						index += 6; //length in bytes + 'STRC'		
							
						int structuresn;	
						if (endianess == "big")
							structuresn = 2*2*2*buf[index+0]+2*2*buf[index+1]+2*buf[index+2]+1*buf[index+3];
						else 
							structuresn = 2*2*2*buf[index+3]+2*2*buf[index+2]+2*buf[index+1]+1*buf[index+0];
						for (int l = 0; l < structuresn; l++) {
						
							index+=2;	
							int fieldsn;
							if (endianess == "big")
								fieldsn = buf[index]*2*2*2+buf[index+1]*2*2+buf[index+2]+2*buf[index+1]+1*buf[index];	
							else
								fieldsn = buf[index+3]*2*2*2+buf[index+2]*2*2+buf[index+1]+2*buf[index+1]+1*buf[index];	

							int p;
							for (p = 0; p < fieldsn ;p++ ) {
							int indexintype;

							if (endianess == "big") {
								indexintype = 2*buf[index+1]+1*buf[index];	
							} else
								indexintype = 2*buf[index+1]+1*buf[index];	
							}
							index += 2;
						}
						index += 2;//for structuresn end	
					}	
				

			}

			return 0;
	//	}	
	//	return -1;
	}

};
