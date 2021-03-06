/*
 * Syncany, www.syncany.org
 * Copyright (C) 2011-2014 Philipp C. Heckel <philipp.heckel@gmail.com> 
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.syncany.plugins.transfer.files;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.syncany.plugins.transfer.RemoteTransaction;
import org.syncany.plugins.transfer.StorageException;

/**
 * The transaction file represents a manifest of a transaction on the remote storage. 
 * 
 * <p><b>Name pattern:</b> The name pattern of a transaction file is
 * <b>transaction-&lt;filehexhashcode&gt;</b>.
 * 
 * @author Pim Otte
 */
public class TransactionRemoteFile extends RemoteFile {
	private static final Pattern NAME_PATTERN = Pattern.compile("transaction-([a-f0-9]+)");
	private static final String NAME_FORMAT = "transaction-%s";

	/**
	 * Initializes a new transaction file, given a name. 
	 * 
	 * @param name transaction file name; <b>must</b> always match the {@link #NAME_PATTERN} 
	 * @throws StorageException If the name is not match the name pattern
	 */
	public TransactionRemoteFile(String name) throws StorageException {
		super(name);
	}

	/**
	 * Initializes a new transaction file, given the transaction itself.
	 * 
	 * @param remoteTransaction the remoteTransaction for which a file is needed
	 * @throws StorageException If the name is not match the name pattern
	 */
	public TransactionRemoteFile(RemoteTransaction remoteTransaction) throws StorageException {
		super(String.format(NAME_FORMAT, Integer.toHexString(remoteTransaction.hashCode())));
	}

	@Override
	protected String validateName(String name) throws StorageException {
		Matcher matcher = NAME_PATTERN.matcher(name);

		if (!matcher.matches()) {
			throw new StorageException(name + ": remote filename pattern does not match: " + NAME_PATTERN.pattern() + " expected.");
		}

		return name;
	}
}
