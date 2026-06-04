package com.paypilot.ledger.storage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("!cloud")
class LocalStorageService implements StorageService {
  private final Path root;

  LocalStorageService(@Value("${paypilot.storage.local-dir}") String localDir) throws IOException {
    this.root = Path.of(localDir).toAbsolutePath().normalize();
    Files.createDirectories(root);
  }

  @Override
  public void write(String key, byte[] data) {
    try {
      Path target = resolve(key);
      Files.createDirectories(target.getParent());
      Files.write(target, data);
    } catch (IOException ex) {
      throw new StorageException("Could not write local storage object " + key, ex);
    }
  }

  @Override
  public byte[] read(String key) {
    try {
      return Files.readAllBytes(resolve(key));
    } catch (IOException ex) {
      throw new StorageException("Could not read local storage object " + key, ex);
    }
  }

  @Override
  public List<String> list(String prefix) {
    try {
      if (!Files.exists(root)) {
        return List.of();
      }
      try (var stream = Files.walk(root)) {
        return stream.filter(Files::isRegularFile)
            .map(root::relativize)
            .map(path -> path.toString().replace('\\', '/'))
            .filter(key -> key.startsWith(prefix))
            .toList();
      }
    } catch (IOException ex) {
      throw new StorageException("Could not list local storage prefix " + prefix, ex);
    }
  }

  @Override
  public void delete(String key) {
    try {
      Files.deleteIfExists(resolve(key));
    } catch (IOException ex) {
      throw new StorageException("Could not delete local storage object " + key, ex);
    }
  }

  private Path resolve(String key) {
    Path target = root.resolve(key).normalize();
    if (!target.startsWith(root)) {
      throw new StorageException("Storage key escapes local root: " + key);
    }
    return target;
  }
}
