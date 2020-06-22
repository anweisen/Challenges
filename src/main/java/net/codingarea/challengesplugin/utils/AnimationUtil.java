package net.codingarea.challengesplugin.utils;

import net.codingarea.challengesplugin.utils.AnimationUtil.InventoryAnimation.AnimationFrame;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

/**
 * @author anweisen & Dominik
 * Challenges developed on 05-29-2020
 * https://github.com/anweisen
 * https://github.com/KxmischesDomi
 */

public class AnimationUtil {

    public static class AnimatedInventory {

        private final InventoryAnimation animation;
        private String title;
        private final int delay;
        private final int size;
        private AnimationSound sound;
        private AnimationSound endSound;

        public AnimatedInventory(@Nonnull String title, int delay, int size, AnimationSound sound) {
            this.title = title;
            this.delay = delay;
            this.size = size;
            this.sound = sound;
            this.animation = new InventoryAnimation();
        }

        public void trigger(final Player player, JavaPlugin plugin) {

            if (player == null) throw new IllegalArgumentException("Player cannot be null!");
            if (plugin == null) throw new IllegalArgumentException("Plugin cannot be null!");

            Inventory inventory = Bukkit.createInventory(null, size, title);
            player.openInventory(inventory);

            for (int i = 0; i < animation.getFrames().size(); i++) {

                final AnimationFrame currentFrame = animation.getFrames().get(i);
                final int I = i;

                Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, () -> {

                    if (!player.getOpenInventory().getTitle().equals(title)) return;

                    if (currentFrame == null) {
                        inventory.setContents(new ItemStack[size]);
                    } else
                        inventory.setContents(currentFrame.getContents());

                    if (sound != null && (currentFrame == null || currentFrame.shouldPlaySound())) {
                        sound.play(player);
                    }
                    if (endSound != null && I == (animation.getFrames().size() - 1)) {
                        endSound.play(player);
                    }

                }, getDelayForFrame(i));

            }

        }

        public void openLastFrame(Player player, boolean sound) {

            Inventory inventory = Bukkit.createInventory(null, size, title);
            AnimationFrame lastFrame = animation.getFrames().get(animation.getFrames().size() - 1);
            inventory.setContents(lastFrame.getContents());
            player.openInventory(inventory);

            if (sound) {
                if (endSound != null) {
                    endSound.play(player);
                } else if (this.sound != null) {
                    this.sound.play(player);
                }
            }

        }

        public AnimatedInventory addFrame(InventoryAnimation.AnimationFrame frame) {
            this.animation.getFrames().add(frame);
            return this;
        }

        public AnimationFrame getFrame(int frame) {
            return this.animation.getFrame(frame);
        }

        public final int getDelayForFrame(int frame) {
            return delay * frame;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {

            if (title == null) throw new IllegalArgumentException("Title cannot be null!");

            this.title = title;

        }

        public InventoryAnimation getAnimation() {
            return animation;
        }

        public void setEndSound(AnimationSound endSound) {
            this.endSound = endSound;
        }
    }

    public static class InventoryAnimation {

        public static class AnimationFrame {

            private boolean sound = true;
            private int size;
            private ItemStack[] contents;

            public AnimationFrame(ItemStack[] contents) {
                setContents(contents);
            }

            public AnimationFrame(int size) {
                this.size = size;
                setContents(null);
            }

            public AnimationFrame setPlaySound(boolean play) {
                this.sound = play;
                return this;
            }

            public AnimationFrame setItem(int slot, ItemStack item) {
                contents[slot] = item;
                return this;
            }

            public AnimationFrame setContents(ItemStack[] contents) {

                if (contents == null) {
                    this.contents = new ItemStack[size];
                } else {
                    this.size = contents.length;
                    this.contents = contents;
                }

                return this;

            }

            public AnimationFrame fill(ItemStack item) {
                for (int i = 0; i < size && i < contents.length; i++) {
                    contents[i] = item;
                }
                return this;
            }

            public ItemStack[] getContents() {
                return contents;
            }

            public boolean shouldPlaySound() {
                return sound;
            }

            @Override
            public AnimationFrame clone() {
                return new AnimationFrame(size).setContents(contents.clone());
            }

        }

        private List<AnimationFrame> frames;

        public InventoryAnimation() {
            frames = new ArrayList<>();
        }

        public InventoryAnimation(List<AnimationFrame> frames) {

            if (frames == null) {
                this.frames = new ArrayList<>();
            } else
                this.frames = frames;
        }

        public List<AnimationFrame> getFrames() {
            return frames;
        }

        public InventoryAnimation setFrames(List<AnimationFrame> frames) {

            if (frames == null) {
                this.frames = new ArrayList<>();
            } else
                this.frames = frames;

            return this;

        }

        public AnimationFrame getFrame(int frame) {
            return frames.get(frame);
        }

    }

    public static class AnimationSound {

        public static final AnimationSound STANDARD_SOUND = new AnimationSound(Sound.BLOCK_WOODEN_BUTTON_CLICK_ON, 0.5F, 1);
        public static final AnimationSound ON_SOUND = new AnimationSound(Sound.BLOCK_NOTE_BLOCK_PLING, 0.5F, 1);
        public static final AnimationSound OFF_SOUND = new AnimationSound(Sound.BLOCK_NOTE_BLOCK_BASS, 0.5F, 1);
        public static final AnimationSound PLING_SOUND = new AnimationSound(Sound.BLOCK_NOTE_BLOCK_BELL, 1, 1);
        public static final AnimationSound OPEN_SOUND = new AnimationSound(Sound.ENTITY_PLAYER_LEVELUP, 0.6F, 2F).addSound(Sound.ENTITY_CHICKEN_EGG, 1F, 2F);
        public static final AnimationSound DEATH_SOUND = new AnimationSound(Sound.ENTITY_BAT_DEATH, 0.7F, 1F);
        public static final AnimationSound TELEPORT_SOUND = new AnimationSound(Sound.ITEM_CHORUS_FRUIT_TELEPORT, 0.9F, 1F);
        public static final AnimationSound PLOP_SOUND = new AnimationSound(Sound.ENTITY_CHICKEN_EGG, 1F, 2F);

        private List<Sound> sound;
        private List<Float> pitch;
        private List<Float> volume;

        public AnimationSound(Sound sound, float volume, float pitch) {
            this.sound = new ArrayList<>(); this.sound.add(sound);
            this.volume = new ArrayList<>(); this.volume.add(volume);
            this.pitch = new ArrayList<>(); this.pitch.add(pitch);
        }

        public AnimationSound addSound(Sound sound, float volume, float pitch) {

            this.sound.add(sound);
            this.volume.add(volume);
            this.pitch.add(pitch);
            return this;

        }

        public void play(Player player) {
            if (player == null) return;
            play(player, player.getLocation());
        }

        public void playDelayed(JavaPlugin plugin, Player player, int delay) {
            if (player == null) return;
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                play(player, player.getLocation());
            }, delay);
        }

        public void broadcast() {
            for (Player currentPlayer : Bukkit.getOnlinePlayers()) {
                play(currentPlayer);
            }
        }

        public void play(Player player, Location location) {
            if (player == null) return;
            for (int i = 0; i < sound.size() && i < volume.size() && i < pitch.size(); i++) {
                player.playSound(location, sound.get(i), volume.get(i), pitch.get(i));
            }
        }

    }

}
