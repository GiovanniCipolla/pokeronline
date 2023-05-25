package it.prova.pokeronline.dto;

import java.util.List;
import java.util.stream.Collectors;

public class SvuotaTavoliDTO {
	
	String username;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public SvuotaTavoliDTO(String username) {
		super();
		this.username = username;
	}

	public SvuotaTavoliDTO() {
		super();
	}
	
	public static List<String> createListStringToDTO(List<SvuotaTavoliDTO> instance){
		return instance.stream().map(entity -> entity.getUsername()).collect(Collectors.toList());
	}
	
}
