package org.deep.websocketextension;

import burp.api.montoya.ui.editor.extension.EditorCreationContext;
import burp.api.montoya.ui.editor.extension.ExtensionProvidedWebSocketMessageEditor;
import burp.api.montoya.ui.editor.extension.WebSocketMessageEditorProvider;
import burp.api.montoya.MontoyaApi;

public class WebsocketMessageEditor implements WebSocketMessageEditorProvider
{
	MontoyaApi api; 
	
	public WebsocketMessageEditor(MontoyaApi api)
	{
		this.api = api;
	}
	
	@Override
	public ExtensionProvidedWebSocketMessageEditor provideMessageEditor(EditorCreationContext creationContext)
	{
		return new websocketExtensionWin(api, creationContext);
	}
}
