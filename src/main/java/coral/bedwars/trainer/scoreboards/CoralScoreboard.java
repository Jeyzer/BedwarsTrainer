package coral.bedwars.trainer.scoreboards;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import coral.bedwars.trainer.Coral;
import coral.bedwars.trainer.abstraction.interfacing.sidebar.Sidebar;
import coral.bedwars.trainer.data.trainers.bridging.Bridging;
import coral.bedwars.trainer.faster.NMS;
import coral.bedwars.trainer.faster.Nullation;
import coral.bedwars.trainer.faster.Spigot;
import net.luckperms.api.model.group.Group;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class CoralScoreboard implements Sidebar<String, Integer, String> {

    private final Player player;
    private final BoardsHandler boardsHandler;
    private Scoreboard scoreboard;
    private Objective objective, buffer;

    private String name;

    private Map<Integer, String> lines = Maps.newHashMap();
    private boolean deleted = false;

    public CoralScoreboard(BoardsHandler boardsHandler, Player player, String name) {
        this(boardsHandler, player, null, name);
    }

    public CoralScoreboard(BoardsHandler boardsHandler, Player player, Scoreboard scoreboard, String name) {
        this.boardsHandler = boardsHandler;
        this.player = player;
        this.scoreboard = scoreboard;

        if (this.scoreboard == null) {
            Scoreboard sb = player.getScoreboard();

            if(sb == null || sb == Bukkit.getScoreboardManager().getMainScoreboard())
                sb = Bukkit.getScoreboardManager().getNewScoreboard();

            this.scoreboard = sb;
        }

        this.name = name;

        String subName = player.getName().length() <= 14
                ? player.getName()
                : player.getName().substring(0, 14);

        this.objective = this.scoreboard.getObjective("sb" + subName);
        this.buffer = this.scoreboard.getObjective("bf" + subName);

        if(this.objective == null)
            this.objective = this.scoreboard.registerNewObjective("sb" + subName, "dummy");
        if(this.buffer == null)
            this.buffer = this.scoreboard.registerNewObjective("bf" + subName, "dummy");

        this.objective.setDisplayName(name);
        sendObjective(this.objective, ObjectiveMode.CREATE);
        sendObjectiveDisplay(this.objective);

        this.buffer.setDisplayName(name);
        sendObjective(this.buffer, ObjectiveMode.CREATE);

        this.player.setScoreboard(this.scoreboard);
    }

    @Override
    public String get(Integer score) {
        if(this.deleted)
            throw new IllegalStateException("The PlayerBoard is deleted!");

        return this.lines.get(score);
    }

    @Override
    public void set(String name, Integer score) {
        if(this.deleted)
            throw new IllegalStateException("The PlayerBoard is deleted!");

        String oldName = this.lines.get(score);

        if(name.equals(oldName))
            return;

        this.lines.entrySet()
                .removeIf(entry -> entry.getValue().equals(name));

        if(oldName != null) {
            if(NMS.getVersion().getMajor().equals("1.7")) {
                sendScore(this.objective, oldName, score, true);
                sendScore(this.objective, name, score, false);
            }
            else {
                sendScore(this.buffer, oldName, score, true);
                sendScore(this.buffer, name, score, false);

                swapBuffers();

                sendScore(this.buffer, oldName, score, true);
                sendScore(this.buffer, name, score, false);
            }
        }
        else {
            sendScore(this.objective, name, score, false);
            sendScore(this.buffer, name, score, false);
        }

        this.lines.put(score, name);
    }

    @Override
    public void setAll(String... lines) {
        if(this.deleted)
            throw new IllegalStateException("The PlayerBoard is deleted!");

        for(int i = 0; i < lines.length; i++) {
            String line = lines[i];

            set(line, lines.length - i);
        }

        Set<Integer> scores = Sets.newHashSet(this.lines.keySet());

        for (int score : scores) {
            if (score <= 0 || score > lines.length) {
                remove(score);
            }
        }
    }

    @Override
    public void clear() {
        Sets.newHashSet(this.lines.keySet()).forEach(this::remove);
        this.lines.clear();
    }

    private void swapBuffers() {
        sendObjectiveDisplay(this.buffer);

        Objective temp = this.buffer;

        this.buffer = this.objective;
        this.objective = temp;
    }

    private void sendObjective(Objective obj, ObjectiveMode mode) {
        try {
            Object objHandle = NMS.getHandle(obj);

            Object packetObj = NMS.PACKET_OBJ.newInstance(
                    objHandle,
                    mode.ordinal()
            );

            NMS.sendPacket(packetObj, player);
        } catch(InstantiationException | IllegalAccessException
                | InvocationTargetException | NoSuchMethodException e) {

            Spigot.log("Error while creating and sending objective packet. (Unsupported Minecraft version?)");
        }
    }

    private void sendObjectiveDisplay(Objective obj) {
        try {
            Object objHandle = NMS.getHandle(obj);

            Object packet = NMS.PACKET_DISPLAY.newInstance(
                    1,
                    objHandle
            );

            NMS.sendPacket(packet, player);
        } catch(InstantiationException | IllegalAccessException
                | InvocationTargetException | NoSuchMethodException e) {

            Spigot.log("Error while creating and sending display packet. (Unsupported Minecraft version?)");
        }
    }

    @SuppressWarnings({"rawtypes"})
    private void sendScore(Objective obj, String name, int score, boolean remove) {
        try {
            Object sbHandle = NMS.getHandle(scoreboard);
            Object objHandle = NMS.getHandle(obj);

            Object sbScore = NMS.SB_SCORE.newInstance(
                    sbHandle,
                    objHandle,
                    name
            );

            NMS.SB_SCORE_SET.invoke(sbScore, score);

            Map scores = (Map) NMS.PLAYER_SCORES.get(sbHandle);

            if(remove) {
                if(scores.containsKey(name))
                    ((Map) scores.get(name)).remove(objHandle);
            }
            else {
                if(!scores.containsKey(name))
                    scores.put(name, new HashMap());

                ((Map) scores.get(name)).put(objHandle, sbScore);
            }

            switch(NMS.getVersion().getMajor()) {
                case "1.7": {
                    Object packet = NMS.PACKET_SCORE.newInstance(
                            sbScore,
                            remove ? 1 : 0
                    );

                    NMS.sendPacket(packet, player);
                    break;
                }

                case "1.8":
                case "1.9":
                case "1.10":
                case "1.11":
                case "1.12": {
                    Object packet;

                    if (remove) {
                        packet = NMS.PACKET_SCORE_REMOVE.newInstance(
                                name,
                                objHandle
                        );
                    }
                    else {
                        packet = NMS.PACKET_SCORE.newInstance(
                                sbScore
                        );
                    }

                    NMS.sendPacket(packet, player);
                    break;
                }

                default: {
                    Object packet = NMS.PACKET_SCORE.newInstance(
                            remove ? NMS.ENUM_SCORE_ACTION_REMOVE : NMS.ENUM_SCORE_ACTION_CHANGE,
                            obj.getName(),
                            name,
                            score
                    );

                    NMS.sendPacket(packet, player);
                    break;
                }
            }
        } catch(InstantiationException | IllegalAccessException
                | InvocationTargetException | NoSuchMethodException e) {

            Spigot.log("Error while creating and sending remove packet. (Unsupported Minecraft version?)");
        }
    }

    private List<String> template() {
        List<String> template = Lists.newArrayList();

        if (player.hasMetadata("bridging")) {
            Bridging.byName(player.getMetadata("bridging").get(0).asString())
                    .flatMap(bridging -> bridging.session(player)).ifPresent(session -> session.sidebarTemplate(template));

            return template;
        }else{
            String group =
                    Nullation.nonNull(Coral.getGroup(player))
                    .get(Group::getDisplayName)
                    .withDefault("Utente")
                    .finalized();

            template.add("§r");
            template.add("§fOnline: §b" + Bukkit.getOnlinePlayers().size());
            template.add("§fIn gioco: §b" + Bridging.sum());
            template.add("§r  ");
            template.add("§fRank: §7" + group);
            template.add("§r ");
            template.add("§eplay.coralmc.it");
        }

        return template;
    }

    public void refresh() {
        setAll(template().toArray(new String[0]));
    }

    @Override
    public void remove(Integer score) {
        if (this.deleted)
            throw new IllegalStateException("The PlayerBoard is deleted!");

        String name = this.lines.get(score);

        Nullation.nonNull(name)
                .then(scoreboard::resetScores)
                .then(() -> lines.remove(score));
    }

    @Override
    public void delete() {
        if (this.deleted)
            return;

        boardsHandler.removeBoard(player);

        sendObjective(this.objective, ObjectiveMode.REMOVE);
        sendObjective(this.buffer, ObjectiveMode.REMOVE);

        this.objective.unregister();
        this.objective = null;

        this.buffer.unregister();
        this.buffer = null;

        this.lines = null;

        this.deleted = true;
    }

    @Override
    public Map<Integer, String> getLines() {
        if(this.deleted)
            throw new IllegalStateException("The PlayerBoard is deleted!");

        return new HashMap<>(lines);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        if(this.deleted)
            throw new IllegalStateException("The PlayerBoard is deleted!");

        this.name = name;

        this.objective.setDisplayName(name);
        this.buffer.setDisplayName(name);

        sendObjective(this.objective, ObjectiveMode.UPDATE);
        sendObjective(this.buffer, ObjectiveMode.UPDATE);
    }

    public Player getPlayer() {
        return player;
    }

    public Scoreboard getScoreboard() { return scoreboard; }

    private enum ObjectiveMode { CREATE, REMOVE, UPDATE }

}
