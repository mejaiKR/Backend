package mejai.mejaigg.app.user.domain;

import java.time.LocalDateTime;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import mejai.mejaigg.global.jpa.BaseEntity;
import mejai.mejaigg.summoner.domain.Summoner;

@Entity
@Table(name = "app_user")
@Getter
@SQLDelete(sql = "UPDATE app_user SET deleted_at = NOW() WHERE id = ?")
@Where(clause = "deleted_at IS NULL")
public class AppUser extends BaseEntity {
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "social_id", nullable = false)
	private String socialId;

	@Enumerated(EnumType.STRING)
	@Column(name = "social_type", nullable = false)
	private SocialType socialType;

	@Enumerated(EnumType.STRING)
	@Column(name = "relationship")
	private Relationship relationship;

	@OneToOne
	@JoinColumn(name = "summoner_id")
	private Summoner watchSummoner;

	@Column(name = "last_updated_watch_summoner", nullable = true)
	private LocalDateTime lastUpdatedWatchSummoner;

	@Column(name = "deleted_at")
	private LocalDateTime deletedAt;

	public AppUser(String socialId, SocialType socialType) {
		this.socialId = socialId;
		this.socialType = socialType;
	}

	public AppUser() {
	}

	public void addWatchSummoner(Summoner summoner, Relationship relationship) {
		this.watchSummoner = summoner;
		this.relationship = relationship;
	}

	public String getRelationship() {
		return relationship.getRelationship();
	}

	public boolean canRefreshWatchSummoner() {
		if (lastUpdatedWatchSummoner == null) {
			return true;
		}
		return lastUpdatedWatchSummoner.plusHours(1).isBefore(LocalDateTime.now());
	}

	public void refreshWatchSummoner() {
		this.lastUpdatedWatchSummoner = LocalDateTime.now();
	}
}
