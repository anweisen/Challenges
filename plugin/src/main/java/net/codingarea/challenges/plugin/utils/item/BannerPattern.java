package net.codingarea.challenges.plugin.utils.item;

import org.bukkit.block.banner.PatternType;

import javax.annotation.Nonnull;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 2.0
 */
public enum BannerPattern {

	BASE_DEXTER_CANTON(PatternType.SQUARE_BOTTOM_LEFT),
	BASE_SINISTER_CANTON(PatternType.SQUARE_BOTTOM_RIGHT),
	CHIEF_DEXTER_CANTON(PatternType.SQUARE_TOP_LEFT),
	CHIEF_SINISTER_CANTON(PatternType.SQUARE_TOP_RIGHT),
	BASE(PatternType.STRIPE_BOTTOM),
	CHIEF(PatternType.STRIPE_TOP),
	PALE_DEXTER(PatternType.STRIPE_LEFT),
	PALE_SINISTER(PatternType.STRIPE_RIGHT),
	PALE(PatternType.STRIPE_CENTER),
	FESS(PatternType.STRIPE_MIDDLE),
	BEND(PatternType.STRIPE_DOWNRIGHT),
	BEND_SINISTER(PatternType.STRIPE_DOWNLEFT),
	PALY(PatternType.STRIPE_SMALL),
	SALTIRE(PatternType.CROSS),
	CROSS(PatternType.STRAIGHT_CROSS),
	CHEVRON(PatternType.TRIANGLE_BOTTOM),
	INVERTED_CHEVRON(PatternType.TRIANGLE_TOP),
	BASE_INDENTED(PatternType.TRIANGLES_BOTTOM),
	CHIEF_INDENTED(PatternType.TRIANGLES_TOP),
	PER_BEND_SINISTER(PatternType.DIAGONAL_LEFT),
	PER_BEND_SINISTER_INVERTED(PatternType.DIAGONAL_RIGHT),
	PER_BEND_INVERTED(PatternType.DIAGONAL_LEFT_MIRROR),
	PER_BEND(PatternType.DIAGONAL_RIGHT_MIRROR),
	ROUNDEL(PatternType.CIRCLE_MIDDLE),
	LOZENGE(PatternType.RHOMBUS_MIDDLE),
	PER_PALE(PatternType.HALF_VERTICAL),
	PER_FESS(PatternType.HALF_HORIZONTAL),
	PER_PALE_INVERTED(PatternType.HALF_VERTICAL_MIRROR),
	PER_FESS_INVERTED(PatternType.HALF_HORIZONTAL_MIRROR),
	BORDURE(PatternType.BORDER),
	BORDURE_INDENTED(PatternType.CURLY_BORDER),
	GRADIENT(PatternType.GRADIENT),
	BASE_GRADIENT(PatternType.GRADIENT_UP),
	FIELD_MASONED(PatternType.BRICKS),
	CREEPER_CHARGE(PatternType.CREEPER),
	SKULL_CHARGE(PatternType.SKULL),
	FLOWER_CHARGE(PatternType.FLOWER),
	MOJANG(PatternType.MOJANG);

	private final PatternType patternType;

	BannerPattern(@Nonnull PatternType patternType) {
		this.patternType = patternType;
	}

	@Nonnull
	public PatternType getPatternType() {
		return patternType;
	}

	@Nonnull
	public String getIdentifier() {
		return patternType.getIdentifier();
	}

}
