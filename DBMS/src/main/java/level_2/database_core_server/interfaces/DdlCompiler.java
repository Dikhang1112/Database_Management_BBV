package level_2.database_core_server.interfaces;

public interface DdlCompiler {
    MetadataEntity compileDdl(String sqlText);
}
