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
package org.syncany.plugins.googledrive;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import org.syncany.plugins.transfer.TransferPlugin;

import java.io.IOException;
import java.util.Arrays;

public class GoogleDriveTransferPlugin extends TransferPlugin {
	private static final String CLIENT_ID = "143404779943-76gn8gao2qoii3cnmiiji6g6qc2msaj6.apps.googleusercontent.com";
	private static final String CLIENT_SECRET = "hNQwo8i-0P0x5KN8cZ6aBMK3";
	private static final String REDIRECT_URI = "urn:ietf:wg:oauth:2.0:oob";

	private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
	private static final JsonFactory JSON_FACTORY = new JacksonFactory();
	private static final GoogleAuthorizationCodeFlow FLOW = new GoogleAuthorizationCodeFlow.Builder(
			HTTP_TRANSPORT, JSON_FACTORY, CLIENT_ID, CLIENT_SECRET, Arrays.asList(DriveScopes.DRIVE))
			.setAccessType("online")
			.setApprovalPrompt("auto").build();


	public GoogleDriveTransferPlugin() {
		super("googledrive");
	}

	public static GoogleDriveClient createClient(String authorizationCode) throws IOException {
		GoogleTokenResponse response = FLOW
				.newTokenRequest(authorizationCode)
				.setRedirectUri(GoogleDriveTransferPlugin.REDIRECT_URI)
				.execute();
		GoogleCredential credential = new GoogleCredential().setFromTokenResponse(response);
		Drive wrappedClient = new Drive.Builder(GoogleDriveTransferPlugin.HTTP_TRANSPORT,
				GoogleDriveTransferPlugin.JSON_FACTORY, credential).build();

		return new GoogleDriveClient(wrappedClient);
	}

	public static String getAuthorizationUrl() {
		return FLOW.newAuthorizationUrl().setRedirectUri(GoogleDriveTransferPlugin.REDIRECT_URI).build();
	}
}
