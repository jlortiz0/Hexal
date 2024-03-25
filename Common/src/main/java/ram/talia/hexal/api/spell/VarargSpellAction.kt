package ram.talia.hexal.api.spell

import at.petrak.hexcasting.api.HexAPI
import at.petrak.hexcasting.api.casting.castables.Action
import at.petrak.hexcasting.api.casting.ParticleSpray
import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.eval.OperationResult
import at.petrak.hexcasting.api.casting.eval.sideeffects.OperatorSideEffect
import at.petrak.hexcasting.api.casting.eval.vm.CastingImage
import at.petrak.hexcasting.api.casting.eval.vm.SpellContinuation
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapNotEnoughArgs
import at.petrak.hexcasting.common.lib.hex.HexEvalSounds

/**
 * An action that has some affect on the world, and takes a variable number of arguments depending on what's on the stack.
 */
interface VarargSpellAction : Action {
    /**
     * The number of arguments that should be accepted from the stack, given the current state of the stack.
     * If there are not enough args for it to be possible, return the smallest number that could be acceptable.
     * [stack] is the reversed stack, so at index 0 is what's on top of the stack, at index 1 is second from the top,
     * etc.
     */
    fun argc(stack: List<Iota>): Int

    fun hasCastingSound(ctx: CastingEnvironment): Boolean = true

    fun awardsCastingStat(ctx: CastingEnvironment): Boolean = true

    fun execute(
            args: List<Iota>,
            argc: Int,
            ctx: CastingEnvironment
    ): Triple<RenderedSpell, Int, List<ParticleSpray>>?

    override fun operate(
        env: CastingEnvironment,
        image: CastingImage,
        continuation: SpellContinuation
    ): OperationResult {
        val stack = image.stack.toMutableList()
        val argc = this.argc(stack.reversed())
        if (argc > stack.size)
            throw MishapNotEnoughArgs(argc, stack.size)
        val args = stack.takeLast(argc)
        for (_i in 0 until argc) stack.removeLast()
        val executeResult = this.execute(args, argc, env) ?: return OperationResult(image, listOf(), continuation, HexEvalSounds.MUTE)
        val (spell, media, particles) = executeResult

        val sideEffects = mutableListOf<OperatorSideEffect>()

        if (media > 0)
            sideEffects.add(OperatorSideEffect.ConsumeMedia(media))

        // Don't have an effect if the caster isn't enlightened, even if processing other side effects
        //if (!isGreat || env.isCasterEnlightened)
            sideEffects.add(
                OperatorSideEffect.AttemptSpell(
                    spell,
                    this.hasCastingSound(env),
                    this.awardsCastingStat(env)
                )
            )

        for (spray in particles)
            sideEffects.add(OperatorSideEffect.Particles(spray))

        val image2 = image.copy(stack = stack, opsConsumed = image.opsConsumed + 1)
        val sound = if (this.hasCastingSound(env)) HexEvalSounds.SPELL else HexEvalSounds.MUTE
        return OperationResult(image2, sideEffects, continuation, sound)
    }
}