/*
 * Copyright (C) 2012 The CyanogenMod Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cyanogenmod.settings.device;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;

public class Utils {

	public static SuperUserProcess sup;

	public Utils() {
		sup = new SuperUserProcess();
	}

	/**
	 * Write a string value to the specified file.
	 * 
	 * @param filename
	 *            The filename
	 * @param value
	 *            The value
	 */
	public static void writeValue(String filename, String value) {
		String command = "echo " + value + " > " + filename;
		// sup = new SuperUserProcess();
		sup.runCommand(command);
	}

	/**
	 * Write a string value to the specified file.
	 * 
	 * @param filename
	 *            The filename
	 * @param value
	 *            The value
	 */
	public static void writeValue(String filename, Boolean value) {
		String sEnvia;
		if (value) {
			sEnvia = "1";
		} else {
			sEnvia = "0";
		}

		String command = "echo " + sEnvia + " > " + filename;
		// sup = new SuperUserProcess();
		sup.runCommand(command);
	}

	/**
	 * Write the "color value" to the specified file. The value is scaled from
	 * an integer to an unsigned integer by multiplying by 2.
	 * 
	 * @param filename
	 *            The filename
	 * @param value
	 *            The value of max value Integer.MAX
	 */
	public static void writeColor(String filename, int value) {
		writeValue(filename, String.valueOf((long) value * 2));
	}

	/**
	 * Check if the specified file exists.
	 * 
	 * @param filename
	 *            The filename
	 * @return Whether the file exists or not
	 */
	public static boolean fileExists(String filename) {
		return new File(filename).exists();
	}

	public static void showDialog(Context ctx, String title, String message) {
		final AlertDialog alertDialog = new AlertDialog.Builder(ctx).create();
		alertDialog.setTitle(title);
		alertDialog.setMessage(message);
		alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						alertDialog.dismiss();
					}
				});
		alertDialog.show();
	}
}

class SuperUserProcess {

	final String TAG = "SuperUserProcess";
	Process p;
	DataOutputStream stdin;

	public SuperUserProcess() {
		try {
			p = Runtime.getRuntime().exec(
					new String[] { "su", "-c", "system/bin/sh" });
			stdin = new DataOutputStream(p.getOutputStream());
		} catch (Exception e) {
			Log.w(TAG, "ERROR: (Running ROOT shell)");
			e.printStackTrace();
		}
	}

	public void runCommand(String command) {

		try {
			stdin.writeBytes(command + "\n");
			stdin.flush();
			//stdin.close();

		} catch (IOException e) {
			Log.w(TAG, "ERROR: (Running ROOT command)");
			e.printStackTrace();
		}
	}
	
	public void closeOutputStream() {
		try {
			stdin.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
