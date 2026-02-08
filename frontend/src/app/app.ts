import {Component, signal, ChangeDetectionStrategy, ChangeDetectorRef} from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { ApiService, Incident, Response } from './api.service';
import {ExecutionTimeComponent} from './components/execution-time/execution-time.component';
import {FilterComponent} from './components/filter/filter.component';
import {ResultsTableComponent} from './components/results-table/results-table.component';
import { TranslateService, TranslateModule } from '@ngx-translate/core';


import { of } from 'rxjs';
import { catchError } from 'rxjs/operators';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, TranslateModule, ExecutionTimeComponent, FilterComponent, ResultsTableComponent],
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

  // Pagination state
  currentPage = 0;
  pageSize = 10;
  totalElements = 0;
  totalPages = 0;
  currentFilters: { title?: string; description?: string; severity?: string; owner?: string } = {};

  constructor(private api: ApiService, private cdr :ChangeDetectorRef, private translate: TranslateService) {}

  onSearch(filters: { title?: string; description?: string; severity?: string; owner?: string }) {
    this.currentFilters = filters;
    this.currentPage = 0; // Reset to first page on new search
    this.fetchIncidents();
  }

  fetchIncidents() {
    this.loading = true;
    this.error = null;
    const start = performance.now();

    this.api.searchIncidents(this.currentFilters, this.currentPage, this.pageSize)
      .pipe(
        catchError((err) => {
          this.error = 'error.generic';
          return of({ content: [], page: { size: this.pageSize, number: 0, totalElements: 0, totalPages: 0 } } as Response<Incident>);
        })
      )
      .subscribe((page) => {
        this.incidents = page.content;
        this.totalElements = page.page.totalElements;
        this.totalPages = page.page.totalPages;
        this.currentPage = page.page.number;
        this.executionTime = (performance.now() - start) / 1000;
        this.loading = false;
        this.cdr.markForCheck();
      });
  }

  changePage(page: number) {
    if (page >= 0 && page < this.totalPages) {
      this.currentPage = page;
      this.fetchIncidents();
    }
  }

  changeSize(size: number) {
    this.pageSize = size;
    this.currentPage = 0; // Reset to first page on size change
    this.fetchIncidents();
  }

  useLang(lang: string) {
    this.translate.use(lang);
  }
}
