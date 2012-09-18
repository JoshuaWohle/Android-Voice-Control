package com.findarato.cyanide;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * @author Team Cyan
 * Interface that all session objects will implement
 * Allows them to be Comparable, cloneable and serializable
 *
 */
public interface Session extends Comparable<Session>, Cloneable, Serializable{
	/**
	 * @return the name of the session
	 */
	public String getName();

	/**
	 * @return a Calendar that will have the date and time of the cleaning session
	 */
	public Calendar getDateTime();
	/**
	 * @return the status of the cleaning session
	 */
	public String getStatus();
	/**
	 * @param status the String passed will become the new status of the cleaning session
	 */
	public void setStatus(String status);
}