package com.longrifle.network;

import com.longrifle.LongRifleMod;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload.Type;

public record FireRiflePayload() implements CustomPacketPayload {
	public static final FireRiflePayload INSTANCE = new FireRiflePayload();
	public static final Type<FireRiflePayload> TYPE = new Type<>(LongRifleMod.id("fire_rifle"));
	public static final StreamCodec<ByteBuf, FireRiflePayload> CODEC = StreamCodec.unit(INSTANCE);

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
