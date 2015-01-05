package net.slimevoid.dynamictransport.entities;

import java.util.HashSet;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.slimevoid.dynamictransport.tileentity.TileEntityTransportComputer;
import net.slimevoid.library.util.helpers.BlockHelper;
import net.slimevoid.library.util.helpers.ChatHelper;

public class EntityMasterElevator extends Entity {
    protected static final float elevatorAccel          = 0.01F;
    protected static final float minElevatorMovingSpeed = 0.016F;

    private BlockPos computerPos;
    private boolean canBeHalted;
    private boolean slowingDown;
    private int startStops;
    private HashSet<EntityElevatorPart> parts ;
    private String elevatorName;
    private String destFloorName;

    public EntityMasterElevator(World world) {
        super(world);
        this.preventEntitySpawning = true;
        this.isImmuneToFire = true;
        this.entityCollisionReduction = 1.0F;
        this.ignoreFrustumCheck = true;
        this.setSize(0F,0F); //this entity doesn't have a bounding box only the parts
        this.parts= new HashSet<EntityElevatorPart>();
        this.motionX = 0.0D;
        this.motionY = 0.0D;
        this.motionZ = 0.0D;
        this.noClip = true;
    }

    public EntityMasterElevator(World world, double x, double y, double z) {
        this(world);
        this.prevPosX = x + 0.5F;
        this.prevPosY = y + 0.5F;
        this.prevPosZ = z + 0.5F;
        this.setPosition(prevPosX,
                prevPosY,
                prevPosZ);
        this.setWaitToAccelerate((byte) 0);

    }

    public void setProperties(int destination, String destinationName, float elevatorTopSpeed, BlockPos computer, boolean haltable, List<BlockPos> elevatorParts ) {
        this.setDestinationY(destination);
        this.destFloorName = destinationName != null
                && !destinationName.trim().equals("") ? destinationName : String.valueOf(destination);

        this.computerPos = computer;
        this.canBeHalted = haltable;
        for (BlockPos elevatorPart : elevatorParts) {
            EntityElevatorPart part = new EntityElevatorPart(this.worldObj, this, elevatorPart.getX(), elevatorPart.getY() + this.posY, elevatorPart.getZ());
            this.worldObj.spawnEntityInWorld(part);
            this.parts.add(part);
        }

        this.setMaximumSpeed(elevatorTopSpeed);

        this.setWaitToAccelerate((byte) 0);
    }

    @Override
    protected void entityInit() {
        this.getDataWatcher().addObject(2,
                -1);
        this.getDataWatcher().addObject(3,
                0f);
        this.getDataWatcher().addObject(4,
                (byte) 0);
        this.getDataWatcher().addObject(5,
                (byte) 0);
    }

    public int getDestinationY() {
        return this.getDataWatcher().getWatchableObjectInt(2);
    }

    protected void setDestinationY(int destinationY) {
        this.getDataWatcher().updateObject(2,
                destinationY);
    }

    public float getMaximumSpeed() {
        return this.getDataWatcher().getWatchableObjectFloat(3);
    }

    protected void setMaximumSpeed(float speed) {
        this.getDataWatcher().updateObject(3,
                speed);
    }

    public byte getWaitToAccelerate() {
        return this.getDataWatcher().getWatchableObjectByte(4);
    }

    public void setWaitToAccelerate(byte value) {
        this.getDataWatcher().updateObject(4, value);
    }

    public boolean getEmerHalt() {
        return this.dataWatcher.getWatchableObjectByte(5) ==  (byte)1;
    }

    public void setEmerHalt(boolean b) {
        this.dataWatcher.updateObject(5, b? (byte)1: (byte)0);
    }

    @Override
    public void setDead() {
        for (EntityElevatorPart part : this.parts) {
            part.setDead(this.getParentElevatorComputerPos());
        }
        if (!this.worldObj.isRemote) {

            if (MathHelper.floor_double(this.posY) == this.getDestinationY()) {
                if (this.elevatorName != null
                        && !this.elevatorName.trim().equals("")) {
                    ChatHelper.sendChatMessageToAllNear(this.worldObj,
                            (int) this.posX,
                            (int) this.posY,
                            (int) this.posZ,
                            4,
                            "slimevoid.DT.entityElevator.arriveWithName",
                            this.elevatorName,
                            this.destFloorName);
                } else {
                    ChatHelper.sendChatMessageToAllNear(this.worldObj,
                            (int) this.posX,
                            (int) this.posY,
                            (int) this.posZ,
                            4,
                            "slimevoid.DT.entityElevator.arrive",
                            this.destFloorName);
                }
                TileEntityTransportComputer computer = this.getParentElevatorComputer();
                if (computer != null) {
                    computer.elevatorArrived(this.getDestinationY());
                }
            }
        }
        super.setDead();
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        //get all the flags

        boolean moving = this.getDestinationY() != -1;
        if (moving) {
            this.moveElevator(); //this calculates the speed of what all child entities will go
        }
        if (!this.worldObj.isRemote) { //this handles all server side logic update riders and world
            boolean removeElevators = this.ticksExisted == 1;
            boolean setTransit = this.ticksExisted % 10 == 0;
            for (EntityElevatorPart part : this.parts) {
                if (removeElevators) part.removeElevatorBlock();
                part.setTransitBlocks();
                if (moving) {
                    part.motionY = this.motionY; //this will allow the client to know how fast the elevator is going
                    part.updateRiderPosition(this.motionY);
                    part.checkMotion(this.motionY, this.getMinElevatorMovingSpeed());
                    part.moveEntity(0, this.motionY, 0);
                }
            }
        }
    }

    protected void moveElevator() {
        if (this.velocityChanged) {
            this.velocityChanged = false;
            this.setEmerHalt(!this.getEmerHalt());

            this.startStops++;
            if (this.startStops > 2) {
                setDead();
            }
        }

        float destinationY = this.getDestinationY() + 0.5F;
        float elevatorSpeed = (float) Math.abs(this.motionY);
        if (this.getEmerHalt()) {
            elevatorSpeed = 0;
        } else {
            if (this.getWaitToAccelerate() < 15) {
                if (this.getWaitToAccelerate() < 10) {
                    elevatorSpeed = 0;
                } else {
                    elevatorSpeed = minElevatorMovingSpeed;
                }
                this.setWaitToAccelerate((byte) (this.getWaitToAccelerate() + 1));

            } else {

                float tempSpeed = Math.min(elevatorSpeed + elevatorAccel, this.getMaximumSpeed());

                // Calculate elevator range to break

                if (!this.slowingDown
                        && MathHelper.abs((float) (destinationY - posY)) >= (tempSpeed
                        * tempSpeed - minElevatorMovingSpeed
                        * minElevatorMovingSpeed)
                        / (2 * elevatorAccel)) {
                    // if current destination is further away than this range and <
                    // max speed, continue to accelerate
                    elevatorSpeed = tempSpeed;
                }
                // else start to slow down
                else {
                    elevatorSpeed -= elevatorAccel;
                    this.slowingDown = true;
                }

                //check lower and upper bounds
                elevatorSpeed = Math.max(Math.min(elevatorSpeed,getMaximumSpeed()),minElevatorMovingSpeed);
            }
        }
        // check whether at the destination or not
        boolean atDestination = this.onGround
                || (MathHelper.abs((float) (destinationY - this.posY)) < elevatorSpeed)
                || (destinationY < 1 || destinationY > this.worldObj.getHeight());

        // if not there yet, update speed and location
        if (!atDestination) {
            this.motionY = (destinationY > this.posY) ? elevatorSpeed : -elevatorSpeed;
            this.moveEntity(this.motionX, this.motionY, this.motionZ);

        } else {
            if(!this.worldObj.isRemote)
                this.setDead();
        }
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound nbttagcompound) {
        nbttagcompound.setInteger("destY", this.getDestinationY());
        if (this.destFloorName != null && !this.destFloorName.trim().isEmpty()) {
            nbttagcompound.setString("destName", this.destFloorName);
        }
        nbttagcompound.setBoolean("emerHalt", this.getEmerHalt());
        nbttagcompound.setInteger("ComputerX", this.computerPos.getX());
        nbttagcompound.setInteger("ComputerY", this.computerPos.getY());
        nbttagcompound.setInteger("ComputerZ", this.computerPos.getZ());
        nbttagcompound.setFloat("TopSpeed", this.getMaximumSpeed());
        //nbttagcompound.setInteger("PartCount",this.parts.size());


    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound nbttagcompound) {
        this.setDestinationY(nbttagcompound.getInteger("destY"));
        this.setMaximumSpeed(nbttagcompound.getFloat("TopSpeed"));
        this.setEmerHalt(nbttagcompound.getBoolean("emerHalt"));
        this.destFloorName = nbttagcompound.getString("destName");
        this.computerPos = new BlockPos(nbttagcompound.getInteger("ComputerX"), nbttagcompound.getInteger("ComputerY"), nbttagcompound.getInteger("ComputerZ"));


    }

    protected TileEntityTransportComputer getParentElevatorComputer() {
        TileEntityTransportComputer computer = null;
        if (this.computerPos != null) {
            computer = (TileEntityTransportComputer) BlockHelper.getTileEntity(this.worldObj,
                    this.computerPos,
                    TileEntityTransportComputer.class);
        }

        return computer;
    }

    protected BlockPos getParentElevatorComputerPos() {
        return this.computerPos;
    }

    public float getMinElevatorMovingSpeed() {
        return minElevatorMovingSpeed;
    }
}
