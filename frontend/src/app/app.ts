import {Component, signal, ChangeDetectionStrategy, ChangeDetectorRef} from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { ApiService, Incident } from './api.service';
import {ExecutionTimeComponent} from './components/execution-time/execution-time.component';
import {FilterComponent} from './components/filter/filter.component';
import {ResultsTableComponent} from './components/results-table/results-table.component';
import {TranslatePipe} from '@ngx-translate/core';


import { of } from 'rxjs';
import { catchError } from 'rxjs/operators';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, ExecutionTimeComponent, FilterComponent, ResultsTableComponent, TranslatePipe],
  changeDetection: ChangeDetectionStrategy.OnPush,
  templateUrl: './app.html',
  styleUrls: ['./app.css']
})
export class App {
  protected readonly title = signal('frontend');
  incidents: Incident[] | null = null;
  executionTime: number | null = null;
  loading = false;
  error: string | null = null;

  constructor(private api: ApiService, private cdr :ChangeDetectorRef) {}

  onSearch(filters: { title?: string; description?: string; severity?: string; owner?: string }) {
    this.loading = true;
    this.error = null;
    const start = performance.now();
    this.api.searchIncidents(filters)
      .pipe(
        catchError((err) => {
          this.error = 'Error while searching for results.';
          return of([] as Incident[]);
        })
      )
      .subscribe((results) => {
        this.incidents = Array.isArray(results) ? [...results] : [];
        this.executionTime = (performance.now() - start) / 1000;
        this.loading = false;
        this.cdr.markForCheck();
      });
  }
}
