import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { delay, of, tap } from 'rxjs';

import { environment } from '../../../environments/environment';
import { Role } from '../models/role.model';
import { InMemoryDatabaseService } from './in-memory-database.service';

@Injectable({ providedIn: 'root' })
export class RoleService {
  private readonly http = inject(HttpClient);
  private readonly db = inject(InMemoryDatabaseService);
  private readonly baseUrl = `${environment.apiBaseUrl}/roles`;
  private readonly useMockData = environment.useMockData;

  readonly roles = this.db.roles;

  load() {
    if (this.shouldUseMock()) {
      return of(this.db.roles()).pipe(delay(100));
    }

    return this.http.get<Role[]>(this.baseUrl).pipe(tap((roles) => this.db.setRoles(roles)));
  }

  private shouldUseMock() {
    return this.useMockData;
  }
}
