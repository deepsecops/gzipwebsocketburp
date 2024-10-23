package org.deep.websocketextension;

import java.awt.Component;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import burp.api.montoya.MontoyaApi;
import burp.api.montoya.core.ByteArray;
import burp.api.montoya.core.ToolSource;
import burp.api.montoya.logging.Logging;
import burp.api.montoya.ui.Selection;
import burp.api.montoya.ui.contextmenu.WebSocketMessage;
import burp.api.montoya.ui.editor.EditorOptions;
import burp.api.montoya.ui.editor.RawEditor;
import burp.api.montoya.ui.editor.extension.EditorCreationContext;
import burp.api.montoya.ui.editor.extension.EditorMode;
import burp.api.montoya.ui.editor.extension.ExtensionProvidedWebSocketMessageEditor;

public class websocketExtensionWin implements ExtensionProvidedWebSocketMessageEditor
{
	EditorCreationContext creationContext;
	RawEditor extensionWin;
	MontoyaApi api; 
	Logging logging; 
	
	public websocketExtensionWin(MontoyaApi api, EditorCreationContext creationContext) {
		this.api = api;
		this.creationContext = creationContext;
		this.logging = api.logging();
		
		ToolSource toolType = creationContext.toolSource();
		
		
		logging.logToOutput(creationContext.editorMode().toString()+"-->");
		
		
	    extensionWin = api.userInterface().createRawEditor();
		
		
	}

	
	
	@Override
	public ByteArray getMessage()
	{
		if (isModified())
		{
			ByteArray modifiedRequest = extensionWin.getContents();
			byte[] modifiedRequestBytes = modifiedRequest.getBytes();
			
			byte[] modifiedRequestBodyCompressed = null;
			try 
			{
				modifiedRequestBodyCompressed = compressBytes(modifiedRequestBytes);
			} 
			catch (IOException e) 
			{	
				e.printStackTrace();
			}
			
			ByteArray modifiedFinalGzippedMessage = ByteArray.byteArray(modifiedRequestBodyCompressed);
			return modifiedFinalGzippedMessage;
			
		}
		return null;
	}
	
	
	@Override
	public void setMessage(WebSocketMessage message)
	{
		ByteArray interceptedRequest = message.payload();
		byte[] byteFromRequest = interceptedRequest.getBytes();
		byte[] decompressedString = GZipDecompress(byteFromRequest);
		ByteArray modifiedPayload = ByteArray.byteArray(decompressedString);
		String s = new String(decompressedString, StandardCharsets.UTF_8);
		
		extensionWin.setContents(modifiedPayload);
	}
	
	@Override
	public boolean isEnabledFor(WebSocketMessage message)
	{
		return true;
	}
	
	@Override
	public String caption()
	{
		return "Text Websocket";
	}
	
	@Override
	public Component uiComponent()
	{
		return extensionWin.uiComponent();
	}
	
	@Override
	public Selection selectedData()
	{
		return null;
	}
	
	@Override
	public boolean isModified()
	{
		return extensionWin.isModified();
	}
	
	//Decompress
    public static byte[] GZipDecompress(byte[] compressedData) 
    {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(compressedData);
        try 
        {
            	GZIPInputStream gzipInputStream = new GZIPInputStream(byteArrayInputStream);
            	ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            	byte[] buffer = new byte[1024*2];
            	int length;
            	while ((length = gzipInputStream.read(buffer)) != -1) 
            	{
            		byteArrayOutputStream.write(buffer, 0, length);
            	}
            	gzipInputStream.close();
            	return byteArrayOutputStream.toByteArray();
        }
        catch (IOException e) 
        {
            e.printStackTrace();
        }
        return null;
    }
    
    //Compress 
   	public static byte[] compressBytes(final byte[] bytes) throws IOException 
   	{
   		if (bytes == null || bytes.length == 0) 
   		{
   			return new byte[0];
   		}
   		final ByteArrayOutputStream out = new ByteArrayOutputStream();
   		try (final GZIPOutputStream gzip = new GZIPOutputStream(out))
   		{
   			gzip.write(bytes);
   		}
   		return out.toByteArray();
   	}

}
