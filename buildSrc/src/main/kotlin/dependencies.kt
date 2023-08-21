import default.DependencyGroup


object GraphQL: DependencyGroup {
    override val groupId get() = "com.expediagroup"
    override val version = "6.3.5"

    val pluginId get() = "com.expediagroup.graphql"

    val kotlinClient get() = dependency("graphql-kotlin-client")
    val kotlinKtorClient get() = dependency("graphql-kotlin-ktor-client")
}
