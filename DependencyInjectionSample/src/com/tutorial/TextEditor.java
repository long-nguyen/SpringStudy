package com.tutorial;

public class TextEditor {
	private SpellChecker spellChecker;
	
	public void setSpellChecker(SpellChecker spellChecker) {
		this.spellChecker = spellChecker;
	}
	
	public SpellChecker getSpellChecker() {
		return spellChecker;
	}
	
	public void spellCheck() {
		spellChecker.checkSpelling();
	}
}
