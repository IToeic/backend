package kr.mojuk.itoeic.word;

import jakarta.persistence.*;

@Entity
@Table(name = "wordpacks")
public class Wordpacks {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "wordpack_id")
    private Integer wordpackId;
	
	@Column(name = "wordpack", length = 100, nullable = false)
	private String wordpack;
	
	public String getWordpack() {
		return wordpack;
	}
	
	public Integer getWordpackId() {
		return wordpackId;
	}
	
	public void setWordpack(String wordpack) {
		this.wordpack = wordpack;
	}
	
	public void setWordpackId(Integer wordpackId) {
		this.wordpackId = wordpackId;
	}
	
	
	
	public Wordpacks() {
	}
	
	public Wordpacks(Integer wordpackId, String wordpack) {
		this.wordpackId = wordpackId;
		this.wordpack = wordpack;
	}
	
}
