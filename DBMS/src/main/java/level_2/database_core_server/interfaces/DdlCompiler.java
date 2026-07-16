package database_core_server.interfaces;

public interface DdlCompiler {
    MetadataEntity compileDdl(String sqlText);
}
