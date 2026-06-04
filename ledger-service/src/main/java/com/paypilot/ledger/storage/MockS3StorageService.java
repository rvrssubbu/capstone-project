package com.paypilot.ledger.storage;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("cloud")
class MockS3StorageService implements StorageService {
  private final Map<String, byte[]> store = new ConcurrentHashMap<>();

  @Override
  public void write(String key, byte[] data) {
    store.put(key, data.clone());
  }

  @Override
  public byte[] read(String key) {
    byte[] data = store.get(key);
    if (data == null) {
      throw new StorageException("Mock S3 object not found: " + key);
    }
    return data.clone();
  }

  @Override
  public List<String> list(String prefix) {
    return store.keySet().stream().filter(key -> key.startsWith(prefix)).sorted().toList();
  }

  @Override
  public void delete(String key) {
    store.remove(key);
  }
}
