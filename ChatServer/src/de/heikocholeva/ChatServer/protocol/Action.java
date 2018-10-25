package de.heikocholeva.ChatServer.protocol;

public enum Action {

	/* Client Actions */
	REGISTER,
	LOGIN,
	SEND_MESSAGE,
	
	/* Server Actions */
	SHOW_ACCESS_KEY,
	ADD_USER,
	REMOVE_USER,
	ADD_MESSAGE,
	ACCESS_KEY_IS_NOT_VALID,
	USERNAME_ALREADY_IN_USE;
}
