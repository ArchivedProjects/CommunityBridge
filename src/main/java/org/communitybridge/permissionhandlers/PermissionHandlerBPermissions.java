package org.communitybridge.permissionhandlers;

import de.bananaco.bpermissions.api.ApiLayer;
import de.bananaco.bpermissions.api.CalculableType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

/**
 * Implements the PermissionHandler interface for bPermissions
 * Notes about bPermissions
 * 2013-May-03: bP does not support the notion of a primary group.
 *
 * @author Feaelin
 */
public class PermissionHandlerBPermissions extends PermissionHandler
{
    public PermissionHandlerBPermissions() throws IllegalStateException
    {
        Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("bPermissions");

        validate(plugin, "bPermissions", "2.9");
    }

    @Override
    public boolean addToGroup(Player player, String groupName)
    {
        ApiLayer.addGroup(determineWorld(player), CalculableType.USER, player.getName(), groupName);

        return true;
    }

    @Override
    public List<String> getGroups(Player player)
    {
        List<String> groups = new ArrayList<String>(Arrays.asList(ApiLayer.getGroups(determineWorld(player), CalculableType.USER, player.getName())));

        return groups;
    }

    @Override
    public List<String> getGroupsPure(Player player)
    {
        List<String> groupList = getGroups(player);

        Iterator<String> group = groupList.iterator();
        while (group.hasNext())
        {
            if (group.next().equalsIgnoreCase("default"))
            {
                group.remove();
            }
        }
        return groupList;
    }

    @Override
    public String getPrimaryGroup(Player player)
    {
        List<String> groups = getGroupsPure(player);

        if (groups.isEmpty())
        {
            return "";
        }
        else
        {
            return groups.get(0);
        }
    }

    @Override
    public boolean isMemberOfGroup(Player player, String groupName)
    {
        return getGroupsPure(player).contains(groupName);
    }

    @Override
    public boolean isPrimaryGroup(Player player, String groupName)
    {
        return groupName.equalsIgnoreCase(getPrimaryGroup(player));
    }

    @Override
    public boolean removeFromGroup(Player player, String groupName)
    {

        ApiLayer.removeGroup(determineWorld(player), CalculableType.USER, player.getName(), groupName);

        return true;
    }

    /**
     * bPermissions doesn't really have a notion of a "primary" group. For now,
     * this simply performs an addToGroup.
     */
    @Override
    public boolean setPrimaryGroup(Player player, String groupName, String formerGroupName)
    {
        boolean result;
        if (formerGroupName == null)
        {
            result = true;
        }
        else
        {
            result = removeFromGroup(player, formerGroupName);
        }
        return result && addToGroup(player, groupName);
    }

    @Override
    public boolean supportsPrimaryGroups()
    {
        return false;
    }
}