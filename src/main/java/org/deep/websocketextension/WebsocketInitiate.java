package org.deep.websocketextension;

import burp.api.montoya.MontoyaApi;
import burp.api.montoya.logging.Logging;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import burp.api.montoya.BurpExtension;


public class WebsocketInitiate implements BurpExtension
{
	
	MontoyaApi api; 
	Logging logging;
	
	@Override
	public void initialize(MontoyaApi api)
	{
		this.api = api;
		this.logging = api.logging();
		api.extension().setName("Testing Extension");
		api.logging().logToOutput("testing the logged message");

		
		//
		
		WebsocketMessageEditor websocketMessageEditor = new WebsocketMessageEditor(api);
		api.userInterface().registerWebSocketMessageEditorProvider(websocketMessageEditor);
	
	}
	
}
