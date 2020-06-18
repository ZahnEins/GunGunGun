package geogun.gungungun;

import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

public final class GunGunGun extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        //ลงทะเบียนอีเว้นที่อยู่ในคลาสนี้
        getServer().getPluginManager().registerEvents(this,this);
    }

    @Override
    public void onDisable() {
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        //รับค่าผู้เล่นที่เข้าเซิฟ
        Player player = event.getPlayer();
        //สร้าง ItemStack เป็นdiamond ขึ้นมา
        ItemStack diamond = new ItemStack(Material.DIAMOND);
        //รับไอเทมเมต้าของไอเทม diamond
        ItemMeta diamonmete = diamond.getItemMeta();
        //ตั้ง Displayname ที่ไอเทมเมต้าว่า GeoGun
        diamonmete.setDisplayName(ChatColor.RED + "GeoGun");
        //เซ็ตไอเทมเมต้าเข้าสู่ diamond
        diamond.setItemMeta(diamonmete);
        //รับค่าคลังผู้เล่นและบวกไอเทมที่สร้างเข้าไป
        player.getInventory().addItem(diamond);
    }

    //Event นี้ตรวจจับเมื่อผู้เล่นกระทำ Action ต่างๆ
    @EventHandler
    public void useGun(PlayerInteractEvent event){
        //เก็บค่าผู้เล่น
        Player player = event.getPlayer();
        //ตรวจสอบว่าคลิกที่ อากาศหรือ ที่ Block หรือไม่
        if(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK){
            //ตรวจสอบไอเทมต้องไม่เท่ากับค่าว่าง [และ] ต้องเป็นไอเทมชนิดไดม่อน [และ] ตรวจสอบว่าไอเทมในมือผู้เล่นนั้นมีชื่อว่า GeoGun หรือไม่
            if(event.getItem() != null && event.getItem().getType() == Material.DIAMOND
                && player.getInventory().getItemInMainHand().getItemMeta().getDisplayName().equals(ChatColor.RED + "GeoGun")){
                //เก็บค่า location ของ player โดยเพิ่ม y มาอีก 1บล็อค คือให้มันสูงพอๆกับหน้าตัวละคร
                Location location = player.getLocation().add(0f,1.1f,0f);
                //ลูประยะทาง i <= 20 ตัวเลข 20 คือ ระยะทางที่ ตรวจจับ
                for(int i = 0; i <= 100 ; i++){
                    //เพิ่มระยะทางที่จะตรวจสอบ Direction คือมุมที่เราหันหน้าไป
                    location = location.add(location.getDirection().getX()
                                ,location.getDirection().getY() + 0.005,location.getDirection().getZ());
                    //player.spawnParticle = spawnParticle นั่นละ
                    player.spawnParticle(Particle.SMOKE_NORMAL,location,0,0.2f,0.2f,0.2f,0);
                    player.spawnParticle(Particle.CRIT_MAGIC,location,0,0.2f,0.2f,0.2f,0);
                    //ตรวจสอบว่า location นั้นๆมี Entities อยู่ใกล้ๆหรือไม่
                    for(Entity entity : location.getWorld().getNearbyEntities(location,0.035f,0.035f,0.035f)){
                        //ถ้ามีก็ทำดาเมจจำนวน ตามที่ใส่ไว้
                        if(entity instanceof LivingEntity){
                            //ทำให้ entity ได้รับดาเมจ 1
                            ((LivingEntity) entity).damage(10);
                        }
                    }
                    //ถ้าชนบล็อคจะหยุดและออกจากลูป
                    if(location.getBlock().getType().isSolid()){
                         break;
                     }
                }
                //เล่นเสียงโกเลมเฮื้อกๆๆๆๆ
                player.playSound(player.getLocation(),Sound.ENTITY_IRON_GOLEM_HURT,2,0.5f);
            }

        }

    }
}
